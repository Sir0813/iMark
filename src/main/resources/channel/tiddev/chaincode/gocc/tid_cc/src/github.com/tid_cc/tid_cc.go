/*
 * 区块链可信身份合约
 * 基于Hyperledger fabric1.4实现
 */

package main

import (
	"crypto"
	_ "crypto"
	"crypto/rand"
	"crypto/rsa"
	"crypto/x509"
	"encoding/base64"
	"encoding/hex"
	"encoding/pem"
	"errors"
	"io/ioutil"
	"strconv"
	"strings"
	"time"

	//"bytes"
	"encoding/json"
	"fmt"
	"log"
	"os"
	//"strconv"
	"github.com/hyperledger/fabric/core/chaincode/shim" //fabric1.4才有，2.0没有，需要重构
	pb "github.com/hyperledger/fabric/protos/peer"
)

var Info = log.New(os.Stdout, "INFO: ", log.Ldate|log.Ltime|log.Lshortfile)
var Error = log.New(os.Stderr, "ERROR: ", log.Ldate|log.Ltime|log.Lshortfile)

const (
	//统一消息常量
	SUCCESS = "200"
	ERROR   = "500"
	EXIST   = "100"
	NOEXIST = "400"

	//统一算法常量
	MD5    = "md5"
	SHA256 = "sha256"
	SHA512 = "sha512"

	ASSET_DEFALE_AMOUNT = "100000" //默认资产发行数量
	ASSET_TRANS_AMOUNT  = "1"     //默认每次资产消耗为1个数量
	ROOT                = "root"  //默认根身份为root
	ASSET_CODE_ISSUE    = "issue" //【资产发行】资产编码
	ASSET_CODE_REL      = "rel"   //【身份关联】资产编码
	ASSET_CODE_KEY      = "key"   //【身份密钥】资产编码
	ASSET_CODE_TID      = "tid"   //【身份保存】资产编码
	MODULE_TID = "tid"
	MODULE_KEY = "key"
	MODULE_REL = "relation"
	//MODULE_ACCOUNT = "account"
	//MODULE_CERT = "cert"
	//MODULE_ACTION = "action"
	//MODULE_ASSET = "asset"
	TYPE_SAVE = "save"
)

var INIT_ASSET_CODE = []string{ASSET_CODE_ISSUE, ASSET_CODE_TID, ASSET_CODE_KEY} //初始化默认发行的资产编码数组
var TID_ASSET_CODE = []string{ASSET_CODE_TID, ASSET_CODE_KEY, ASSET_CODE_REL}    //创建身份需要默认发行的资产编码数组

// TIDChaincode example simple Chaincode implementation
type TIDChaincode struct {
}

//可信身份对象结构
type TID struct { //Go struct处理json格式，[`]与普通单引号[']不一样，则由键盘“~”输入，务必注意否则编译不了
	No       string `json:"no"`       //编号，链上唯一可识标识，hash加密
	Name     string `json:"name"`     //名称，hash加密
	Password string `json:"password"` //密码，hash加密，弱密钥，当配置非对称强密钥时失效，主要用于修改、强密钥配置校验
	//Type    string   `json:"type"` //类型，如：0（个人）/1（组织）/2(应用)
	Mobile string `json:"mobile"` //移动电话，加密
	Email  string `json:"email"`  //邮箱，hash加密
	//Fax    string   `json:"fax"` //传真，hash加密
	Country  string `json:"country"`  //国家
	Province string `json:"province"` //省份
	City     string `json:"city"`     //城市
	Address  string `json:"address"`  //详细地址，加密
	Desc     string `json:"desc"`     //描述
	Data     string `json:"data"`     //扩展数据，可选，是否加密由外部定义，可以是JSON字符内容
	User    User   `json:"user"`    //用户信息，特有用户类型信息
	Org      Org    `json:"org"`      //组织信息，特有组织类型信息
	App      App    `json:"app"`      //应用信息，特有应用类型信息
	Time     string `json:"time"`     //上链时间，即交易时间stub.GetTxTimestamp()
	Txid     string `json:"txid"`     //交易ID，当前交易生成的ID，stub.GetTxID()，可以查询链上位置及详细信息
}

//用户类信息结构
type User struct { //Go struct处理json格式，[`]与普通单引号[']不一样，则由键盘“~”输入，务必注意否则编译不了
	Sex    string `json:"sex"`    //性别,0男/1女
	Birth  string `json:"birth"`  //出生日期
	Nation string `json:"nation"` //民族
	Photo  string `json:"photo"`  //照片，base64
}

//组织类信息结构
type Org struct {
}

//应用类信息结构
type App struct {
}

//身份关联帐户结构，可以任意绑定多个手机号或邮箱或自定义编号等，以便绑定帐号登录时，查询对应身份信息
type Account struct {
	Account string `json:"account"` //关联帐号，可以是手机号或邮箱或自定义编号等，hash加密
	Id      string `json:"id"`      //身份ID
	Time    string `json:"time"`    //上链时间，即交易时间stub.GetTxTimestamp()
	Txid    string `json:"txid"`    //交易ID，当前交易生成的ID，stub.GetTxID()，可以查询链上位置及详细信息
}

//身份密钥结构
type Key struct {
	PublickKey string `json:"publicKey"` //公钥
	Id         string `json:"id"`        //身份ID
	Algorithm  string `json:"algorithm"` //算法
	Time       string `json:"time"`      //上链时间，即交易时间stub.GetTxTimestamp()
	Txid       string `json:"txid"`      //交易ID，当前交易生成的ID，stub.GetTxID()，可以查询链上位置及详细信息
}

//证书结构
type Cert struct { //Go struct处理json格式，[`]与普通单引号[']不一样，则由键盘“~”输入，务必注意否则编译不了
	KeyId         string   `json:"keyId"`         //被认证的身份密钥ID
	CertKeyId     string   `json:"certKeyId"`     //认证身份密钥ID
	Validity      Validity `json:"validity"`      //有效期
	Level         string   `json:"level"`         //当前级别，即当前证书所在级别，0为根证书
	SubLevel      string   `json:"subLevel"`      //子级，即当前证书下允许认证的子级证书多少级
	State         string   `json:"state"`         //状态，正常/吊销
	Sign          string   `json:"sign"`          //签名值，使用指定摘要算法加密后签名后的内容
	HashAlgorithm string   `json:"hashAlgorithm"` //摘要算法，MD5/SHA256/SHA512/SM3
	Time          string   `json:"time"`          //上链时间，即交易时间stub.GetTxTimestamp()
	Txid          string   `json:"txid"`          //交易ID，当前交易生成的ID，stub.GetTxID()，可以查询链上位置及详细信息
}

//有效期
type Validity struct {
	Start string `json:"start"` //开始时间
	End   string `json:"end"`   //结束时间
}

//身份关联，身份之间关系
type Rel struct {
	Id    string `json:"id"`    //身份ID
	RelId string `json:"relId"` //关联身份ID
	Alias string `json:"alias"` //别名，在关联身份中的名称
	Time  string `json:"time"`  //上链时间，即交易时间stub.GetTxTimestamp()
	Txid  string `json:"txid"`  //交易ID，当前交易生成的ID，stub.GetTxID()，可以查询链上位置及详细信息
}

//行为结构
type Action struct { //Go struct处理json格式，[`]与普通单引号[']不一样，则由键盘“~”输入，务必注意否则编译不了
	Operator    string `json:"operator"`    //操作者身份ID
	Module        string `json:"module"`        //模块，如：tid/key/relation/account/action等
	Type        string `json:"type"`        //操作类型，如：save/get/quer/delete/add/update等
	Object      string `json:"object"`      //对象ID，如身份/密钥/证书/关联等ID
	//Sign        string `json:"sign"`        //签名值
	Desc		string `json:"desc"` //行为描述
	Time        string `json:"time"`        //上链时间，即交易时间stub.GetTxTimestamp()
	Txid        string `json:"txid"`        //交易ID，当前交易生成的ID，stub.GetTxID()，可以查询链上位置及详细信息
}

//身份关联临时结构，接收输入参数的json数据转换的临时对象
type InputKey struct {
	Key           Key      `json:"key"`           //身份密钥
	HashAlgorithm string   `json:"hashAlgorithm"` //hash算法
	Sign          string   `json:"sign"`          //自验签名，当前公钥对应私钥自签的签名值，以便验证是自身公钥
	Auth          Auth `json:"auth"`        //单个身份授权信息，即对一个key数据进行签名

}

//身份关联临时结构，接收输入参数的json数据转换的临时对象
type InputRel struct {
	Rel      Rel      `json:"rel"`    //身份关联
	Auth Auth `json:"auth"` //单个身份授权信息，即对一个rel数据进行签名
}

//身份关联临时结构，接收输入参数的json数据转换的临时对象
type InputTID struct {
	TID      TID    `json:"tid"`    //身份关联
	Auth     Auth   `json:"auth"` //单个身份授权信息，即对一个tid数据进行签名
}

//身份集合结构
type TIDSet struct {
	Tids []InputTID `json:"tids"` //身份数组
	Keys []InputKey `json:"keys"` //密钥数组
}

//身份密钥集合结构
type KeySet struct {
	Keys []InputKey `json:"keys"` //密钥数组
}

//身份关联集合结构
type RelSet struct {
	Rels []InputRel `json:"rels"` //身份关联数组
}

//资产结构，为每个身份的操作类型发行相应类型资产,从而通过资产控制操作权限
type Asset struct { //Go struct处理json格式，[`]与普通单引号[']不一样，则由键盘“~”输入，务必注意否则编译不了
	Id     string `json:"id"`     //资产所属身份ID
	Code   string `json:"code"`   //资产编码，如new/update等操作类型名称
	Name   string `json:"name"`   //资产名称，如新增XXX身份等
	Amount string `json:"amount"` //发行总量
	Desc   string `json:"desc"`   //说明
	Time   string `json:"time"`   //上链时间，即交易时间stub.GetTxTimestamp()
	Txid   string `json:"txid"`   //交易ID，当前交易生成的ID，stub.GetTxID()，可以查询链上位置及详细信息
}

//钱包结构，存储每个身份持有的资产数额
type Wallet struct { //Go struct处理json格式，[`]与普通单引号[']不一样，则由键盘“~”输入，务必注意否则编译不了
	Owner      string `json:"owner"`      //持有身份ID
	AssetOwner string `json:"assetOwner"` //资产定义中所属身份ID
	AssetCode  string `json:"assetCode"`  //资产定义中编码
	Amount     string `json:"amount"`     //持有数量
	Desc       string `json:"desc"`       //说明
	Time       string `json:"time"`       //上链时间，即交易时间stub.GetTxTimestamp()
	Txid       string `json:"txid"`       //交易ID，当前交易生成的ID，stub.GetTxID()，可以查询链上位置及详细信息
}

//资产交易结构
type Transaction struct {
	Id     string `json:"id"`     //资产所属身份ID
	Code   string `json:"code"`   //资产编码
	From   string `json:"from"`   //交易发起身份ID
	To     string `json:"to"`     //交易目标身份ID
	Amount string `json:"amount"` //交易数量
}

//身份授权结构，两种方式：一是密码，弱密钥，通过id和password配合使用；二是公钥，强密钥，通过hash,公钥，签名配合使用
type Auth struct {
	Id            string `json:"id"`            //身份
	Password      string `json:"password"`      //密码（弱密钥）
	HashAlgorithm string `json:"hashAlgorithm"` //hash算法
	PublicKey     string `json:"publicKey"`     //公钥（强密钥）
	Sign          string `json:"sign"`          //签名值
}

//资产交易输入结构   informats
type InputTransaction struct {
	Transaction Transaction `json:"transaction"` //资产交易信息
	Auth   Auth   `json:"auth"`      //身份授权信息
}

//资产交易集合
type InputTransactionSet struct {
	Transactions []InputTransaction `json:"transactions"` //资产交易数组
}

//资产输入结构
type InputAsset struct {
	Asset    Asset    `json:"asset"`  //资产
	Auth Auth `json:"auth"` //身份授权信息
}

//资产集合
type InputAssetSet struct {
	InputAssets []InputAsset `json:"assets"` //资产交易数组
}

/*********** 入口方法，默认实现接口方法*******/
// Init initializes the chaincode state
//简化处理，暂无初始化，默认为空，否则合约初始化或升级时需要模拟初始化就会报错
func (t *TIDChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	Info.Println("########### TID Init ###########")
	_, args := stub.GetFunctionAndParameters()
	if len(args) != 4 {
		return IError("Incorrect number of arguments. Expecting 4, function followed by [password,amount,publickey,algorithm]", strings.Join(args,","))
	}
	//1. 保存根身份
	tid := TID{}
	tid.No = ROOT
	tid.Password = args[0]
	tid.Name = "根身份"
	res1 := t.saveTID(stub, tid)
	if res1.Status != shim.OK {
		return res1
	}
	//2. 发行根身份资产
	res := t.issueRootAsset(stub, args[1])
	if res.Status != shim.OK {
		return res
	}

	//3. 保存根身份密钥
	key := Key{}
	key.Id = ROOT
	key.PublickKey = args[2]
	key.Algorithm = args[3]
	if len(key.PublickKey) != 0 && len(key.Algorithm) != 0 {
		res = t.saveKey(stub, key)
		if res.Status != shim.OK {
			return res
		}
	}
	return res1
}

// Invoke makes payment of X units from A to B
func (t *TIDChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	Info.Println("########### TID Invoke ###########")
	function, args := stub.GetFunctionAndParameters()
	if function == "save" {
		// Deletes an entity from its state
		return t.save(stub, args)
	}
	if function == "issue" {
		// Deletes an entity from its state
		return t.issue(stub, args)
	}
	if function == "transaction" {
		// Deletes an entity from its state
		return t.transaction(stub, args)
	}
	if function == "key" {
		// Deletes an entity from its state
		return t.key(stub, args)
	}
	if function == "rel" {
		// Deletes an entity from its state
		return t.rel(stub, args)
	}
	if function == "get" {
		// queries an entity state
		return t.get(stub, args)
	}
	if function == "getByKey" {
		// queries an entity state
		return t.getByKey(stub, args)
	}
	/*if function == "delete" {
		// Deletes an entity from its state
		return t.delete(stub, args)
	}
	if function == "deleteByKey" {
		// Deletes an entity from its state
		return t.deleteByKey(stub, args)
	}*/
	Error.Printf("Unknown action, check the first argument, must be one of `delete`, `query`, or `save`. But got: %v", function)
	return shim.Error(fmt.Sprintf("Unknown action, check the first argument, must be one of `delete`, `query`, or `move`. But got: %v", function))
}

/************** 外部方法 ******************/
//身份保存
func (t *TIDChaincode) save(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return IError("Incorrect number of arguments. Expecting 1, function followed by [data]", "")
	}
	inData := args[0] //tid json数据
	fmt.Println(inData)
	dataByte := []byte(inData)
	var tidSet = TIDSet{}
	err := json.Unmarshal(dataByte, &tidSet) //输入json数据反序化成TID对象
	if err != nil {
		return IError("Failed to Unmarshal json", inData)
	}
	//批量新增身份
	tids := tidSet.Tids
	res := t.saveTIDs(stub, tids)
	if res.Status != shim.OK {
		return res
	}
	//批量保存身份密钥
	res = t.saveKeys(stub, tidSet.Keys)
	if res.Status != shim.OK {
		return res
	}
	return res
}

//身份关联(绑定)账户保存
func (t *TIDChaincode) account(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return IError("Incorrect number of arguments. Expecting 4, function followed by [data]", "")
	}
	inData := args[0]
	dataByte := []byte(inData)
	var account = Account{}
	err := json.Unmarshal(dataByte, &account) //输入数据json反序化成TID对象
	if err != nil {
		return IError("Failed to Unmarshal json", inData)
	}
	inAccount := account.Account
	key := GenKey(inAccount)
	//判断是否存在记录,存在则提示错误
	res := BaseNoExist(stub, key)
	if res.GetStatus() != shim.OK {
		return res
	}
	account.Account = key

	//查询身份是否存在,不存在则报错
	inId := account.Id
	id := GenKey(inId) //输入的身份id生成的组成信息，通常为编号
	res = BaseGetByKey(stub, id)
	if res.GetStatus() != shim.OK {
		return res
	}
	account.Id = id
	account.Txid = stub.GetTxID()                      //交易ID
	timestamp, _ := stub.GetTxTimestamp()              //交易时间戳
	ts := TimeSecondToString24(timestamp.Seconds) //转换成标准时间
	account.Time = ts
	return BaseSerializeSave(stub, key, account)
}

//身份密钥（公钥）保存
func (t *TIDChaincode) key(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return IError("Incorrect number of arguments. Expecting 4, function followed by [data]", "")
	}
	inData := args[0]
	fmt.Println("======key========="+inData)
	dataByte := []byte(inData)
	var keySet = KeySet{}
	err := json.Unmarshal(dataByte, &keySet) //输入json数据反序化成TID对象
	if err != nil {
		return IError("Failed to Unmarshal json", inData)
	}
	//批量保存身份密钥
	res := t.saveKeys(stub, keySet.Keys)
	if res.Status != shim.OK {
		return res
	}
	return res
}

//身份密钥（公钥）证书颁发
func (t *TIDChaincode) cert(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 2 {
		return IError("Incorrect number of arguments. Expecting 2, function followed by [data,sign]", "")
	}
	inData := args[0]
	sign := args[1]
	dataByte := []byte(inData)
	var cert = Cert{}
	err := json.Unmarshal(dataByte, &cert) //输入数据json反序化成实体
	if err != nil {
		return IError("Failed to Unmarshal json", inData)
	}
	key := GetSHA256HashCodeInString(cert.KeyId + cert.CertKeyId) //key由主体密钥（公钥） + 认证密钥（公钥）
	//判断是否存在记录,先不判断，不论是否存在都进行存储，不存在则新增，存在则更新

	//查询身份是否存在，不存在报错
	keyId := GenKey(cert.KeyId) //输入参数中属性keyId为被认证的密钥（公钥）
	res := BaseGetByKey(stub, keyId)
	if res.Status != shim.OK {
		return res
	}
	cert.KeyId = keyId

	//查询关联身份是否存在，不存在报错
	certKeyId := GenKey(cert.CertKeyId) //输入参数中属性certKeyId为认证的密钥（公钥）
	res = BaseGetByKey(stub, certKeyId)
	if res.Status != shim.OK {
		return res
	}
	cert.CertKeyId = certKeyId
	//转换成认证的证书实体
	var certKey = Key{}
	certKeyObj, err := stub.GetState(certKeyId)
	err = json.Unmarshal(certKeyObj, &certKey)
	if err != nil {
		return IError("Failed to Unmarshal json", string(certKeyObj))
	}
	//hashAlgorithm
	hashAlgorithm := cert.HashAlgorithm
	if strings.TrimSpace(hashAlgorithm) == "" { //为空则默认为sha256
		hashAlgorithm = SHA256
	}

	//验签
	err = VerifyBase64(dataByte, sign, certKey.PublickKey, hashAlgorithm, certKey.Algorithm)
	if err != nil {
		return IError("Verify Failed", inData)
	}

	cert.Txid = stub.GetTxID()                         //交易ID
	timestamp, _ := stub.GetTxTimestamp()              //交易时间戳
	ts := TimeSecondToString24(timestamp.Seconds) //转换成标准时间
	cert.Time = ts
	return BaseSerializeSave(stub, key, cert)
}

//身份关联
func (t *TIDChaincode) rel(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return IError("Incorrect number of arguments. Expecting 4, function followed by [data]", "")
	}
	inData := args[0]
	dataByte := []byte(inData)
	var relSet = RelSet{}
	err := json.Unmarshal(dataByte, &relSet) //输入json数据反序化成TID对象
	if err != nil {
		return IError("Failed to Unmarshal json", inData)
	}
	//批量保存身份密钥
	res := t.saveRels(stub, relSet.Rels)
	if res.Status != shim.OK {
		return res
	}
	return res
}

//id加密后删除
func (t *TIDChaincode) delete(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return IError("Incorrect number of arguments. Expecting 1", "")
	}
	id := args[0] //key原值，即未hash加密的明文
	return BaseDelete(stub, id)
}

//删除
func (t *TIDChaincode) deleteByKey(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return IError("Incorrect number of arguments. Expecting 1", "")
	}
	key := args[0]
	return BaseDeleteByKey(stub, key)
}

//获取
func (t *TIDChaincode) getByKey(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return IError("Incorrect number of arguments. Expecting 1", "")
	}
	key := args[0] //key原值，即未hash加密的明文
	return BaseGetByKey(stub, key)
}

//id加密后获取
func (t *TIDChaincode) get(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	if len(args) != 1 {
		return IError("Incorrect number of arguments. Expecting 1", "")
	}
	id := args[0] //key原值，即未hash加密的明文
	return BaseGet(stub, id)
}

//资产发行,支持批量发行
func (t *TIDChaincode) issue(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	inData := args[0] //tid json数据
	fmt.Println(inData)
	dataBytes := []byte(inData)
	var assetSet = InputAssetSet{}
	err := json.Unmarshal(dataBytes, &assetSet) //输入json数据反序化成TID对象
	if err != nil {
		return IError("Data failed to Unmarshal", string(dataBytes))
	}
	for i := 0; i < len(assetSet.InputAssets); i++ {
		res := t.issueAsset(stub, assetSet.InputAssets[i], true)
		if res.Status != shim.OK {
			return res
		}
	}
	return ISuccess("Success", inData)
}

//资产交易，支持批量交易
func (t *TIDChaincode) transaction(stub shim.ChaincodeStubInterface, args []string) pb.Response {
	inData := args[0] //tid json数据
	fmt.Println(inData)
	dataBytes := []byte(inData)
	var transSet = InputTransactionSet{}
	err := json.Unmarshal(dataBytes, &transSet) //输入json数据反序化成TID对象
	if err != nil {
		return IError("Data failed to Unmarshal", string(dataBytes))
	}
	for i := 0; i < len(transSet.Transactions); i++ {
		res := t.transAsset(stub, transSet.Transactions[i], true)
		if res.Status != shim.OK {
			return res
		}
	}
	return ISuccess("Success", inData)
}

/********以下为内部方法**********/


//生成key,把输入字符数组组成字符串作为生成key的原字符串
func (t *TIDChaincode) genTIDKey(tid TID) string {
	return GenKey(tid.No)
}

//批量保存身份
func (t *TIDChaincode) saveTIDs(stub shim.ChaincodeStubInterface, tids []InputTID) pb.Response {
	for i := 0; i < len(tids); i++ {
		res := t.checkTID(stub, tids[i].TID, tids[i].Auth,true)
		if res.Status != shim.OK {
			return res
		}
		res = t.saveTID(stub,tids[i].TID)
		if res.Status != shim.OK {
			return res
		}
		//保存行为
		action := Action{}
		action.Operator = tids[i].Auth.Id
		action.Module = MODULE_TID
		action.Type = TYPE_SAVE
		action.Object = t.genTIDKey(tids[i].TID)
		res = t.saveAction(stub,action)
		if res.Status != shim.OK {
			return res
		}
	}
	return ISuccess("Success save all tid", "")
}

//检查身份
func (t *TIDChaincode) checkTID(stub shim.ChaincodeStubInterface, tid TID, auth Auth, isVerify bool) pb.Response {
	//1. 身份认证，对发行的资产所属身份进行认证
	if isVerify {
		tidBytes, err1 := json.Marshal(tid)
		if err1 != nil {
			return IError("Data failed to Unmarshal", string(tidBytes))
		}
		res1 := t.verifyAuth(stub, auth, tidBytes)
		if res1.Status != shim.OK {
			return res1
		}
	}
	//2. 身份保存的权限认证，即需要发起【身份保存】资产交易
	inputTransaction := InputTransaction{}
	transaction := inputTransaction.Transaction
	transaction.Code = ASSET_CODE_TID
	transaction.From = auth.Id
	transaction.Amount = ASSET_TRANS_AMOUNT
	//生成key
	key := t.genTIDKey(tid)
	tidData, err2 := stub.GetState(key)
	if err2 != nil {
		return IError("Failed to get State", tid.No)
	}
	if tidData == nil { //空为新增，则交易根身份发行的【身份保存】资产
		transaction.Id = ROOT //根身份
		transaction.To = ROOT //根身份，资产回收
		//新建身份，则默认发行资产
		res1 := t.issueIdAsset(stub, tid.No, ASSET_DEFALE_AMOUNT)
		if res1.Status != shim.OK {
			return res1
		}
	} else { //更新，则交易当前更新的身份发行的【身份保存】资产
		transaction.Id = tid.No
		transaction.To = tid.No
	}
	inputTransaction.Transaction = transaction
	res := t.transAsset(stub, inputTransaction, false)
	if res.Status != shim.OK {
		return res
	}
	return ISuccess("Success Check, Tid can save","")
}

//保存身份
func (t *TIDChaincode) saveTID(stub shim.ChaincodeStubInterface, tid TID) pb.Response {
	//生成key
	key := t.genTIDKey(tid)
	tid.No = GetSHA256HashCodeInString(tid.No)
	tid.Name = GetSHA256HashCodeInString(tid.Name)
	tid.Password = GetSHA256HashCodeInString(tid.Password)
	tid.Txid = stub.GetTxID()                          //交易ID
	timestamp, _ := stub.GetTxTimestamp()              //交易时间戳
	ts := TimeSecondToString24(timestamp.Seconds) //转换成标准时间
	tid.Time = ts
	res := BaseSerializeSave(stub, key, tid)
	return res
}

//初始发行根身份资产
func (t *TIDChaincode) issueRootAsset(stub shim.ChaincodeStubInterface, amount string) pb.Response {
	inputAsset := InputAsset{}
	asset := Asset{}
	asset.Id = ROOT
	asset.Amount = amount
	for i := 0; i < len(INIT_ASSET_CODE); i++ {
		asset.Code = INIT_ASSET_CODE[i]
		inputAsset.Asset = asset
		res := t.issueAsset(stub, inputAsset, false)
		if res.Status != shim.OK {
			return res
		}
	}
	return ISuccess("Success", amount)
}

//发行普通身份初始资产
func (t *TIDChaincode) issueIdAsset(stub shim.ChaincodeStubInterface, id string, amount string) pb.Response {
	inputAsset := InputAsset{}
	asset := Asset{}
	asset.Id = id
	asset.Amount = amount
	for i := 0; i < len(TID_ASSET_CODE); i++ {
		asset.Code = TID_ASSET_CODE[i]
		inputAsset.Asset = asset
		res := t.issueAsset(stub, inputAsset, false)
		if res.Status != shim.OK {
			return res
		}
	}
	return ISuccess("Success", amount)
}

//资产发行，单个资产发行
func (t *TIDChaincode) issueAsset(stub shim.ChaincodeStubInterface, inputAsset InputAsset, isVerify bool) pb.Response {
	asset := inputAsset.Asset
	assetBytes, err1 := json.Marshal(asset)
	if err1 != nil {
		return IError("Data failed to Unmarshal", string(assetBytes))
	}
	//1. 身份及权限认证
	if isVerify {
		auth := inputAsset.Auth
		//1.1. 身份认证，对发行的资产所属身份进行认证
		res := t.verifyAuth(stub, auth, assetBytes)
		if res.Status != shim.OK {
			return res
		}
		//1.2. 发行权限认证，即需要发起【发行】资产操作对应的资产进行交易
		inputTransaction := InputTransaction{}
		transaction := inputTransaction.Transaction
		transaction.Id = ROOT //只有对根身份发行了【发行】资产
		transaction.Code = ASSET_CODE_ISSUE
		transaction.From = auth.Id
		transaction.To = ROOT //资产发行操作一次，则回收一个数量的【发行】资产
		transaction.Amount = ASSET_TRANS_AMOUNT
		inputTransaction.Transaction = transaction
		res = t.transAsset(stub, inputTransaction, false)
		if res.Status != shim.OK {
			return res
		}
	}
	//2. 更新资产
	res := t.saveAsset(stub, asset)
	if res.Status != shim.OK {
		return res
	}
	//3. 更新钱包
	wallet := Wallet{}
	wallet.Owner = asset.Id
	wallet.AssetOwner = asset.Id
	wallet.AssetCode = asset.Code
	wallet.Amount = asset.Amount
	//wallet.Desc = ""
	res = t.saveWallet(stub, wallet)
	return res
}

//资产交易保存，单笔交易
func (t *TIDChaincode) transAsset(stub shim.ChaincodeStubInterface, inputTransaction InputTransaction, isVerify bool) pb.Response {
	transaction := inputTransaction.Transaction
	transactionBytes, err1 := json.Marshal(transaction)
	if err1 != nil {
		return IError("Data failed to Unmarshal", string(transactionBytes))
	}
	auth := inputTransaction.Auth
	//1. 身份认证，对交易发起方进行身份认证
	if isVerify {
		auth.Id = transaction.From  //确保是使用交易发起方的身份进行认证
		res1 := t.verifyAuth(stub, auth, transactionBytes) //此处ID为transaction.From或auth.Id，两者意义一样
		if res1.Status != shim.OK {
			return res1
		}
	}
	//2. 更新交易发起方钱包
	wallet := Wallet{}
	wallet.Owner = transaction.From
	wallet.AssetOwner = transaction.Id
	wallet.AssetCode = transaction.Code
	wallet.Amount = "-" + transaction.Amount //因为发起方要减掉
	//wallet.Desc = ""
	res := t.saveWallet(stub, wallet)
	if res.Status != shim.OK {
		return res
	}
	//3. 更新交易接收方钱包
	wallet.Owner = transaction.To
	wallet.Amount = transaction.Amount //因为发起方要减掉
	res = t.saveWallet(stub, wallet)
	if res.Status != shim.OK {
		return res
	}
	return ISuccess("Success", string(transactionBytes))
}

//保存资产
func (t *TIDChaincode) saveAsset(stub shim.ChaincodeStubInterface, asset Asset) pb.Response {
	amount, err2 := strconv.Atoi(asset.Amount)
	if err2 != nil {
		return IError("Amount failed to trans to int", asset.Amount)
	}
	if amount < 0 { //资产只能增不能减
		return IError("Amount is Less than 0", asset.Amount)
	}
	key := GenKey(asset.Id, asset.Code)
	dataBytes, err := stub.GetState(key)
	if err != nil {
		return IError("Failed to get State", key)
	}
	if dataBytes != nil { //若存在资产，则新发行数量要加上已有数量
		existAsset := Asset{}
		err = json.Unmarshal(dataBytes, &existAsset)
		if err != nil {
			return IError("Data failed to Unmarshal Asset", string(dataBytes))
		}
		existAmount, err1 := strconv.Atoi(existAsset.Amount)
		if err1 != nil {
			return IError("Amount failed to trans to int", existAsset.Amount)
		}
		amount = existAmount + amount
	}
	asset.Id = GetSHA256HashCodeInString(asset.Id)
	asset.Amount = strconv.Itoa(amount)
	asset.Txid = stub.GetTxID()                        //交易ID
	timestamp, _ := stub.GetTxTimestamp()              //交易时间戳
	ts := TimeSecondToString24(timestamp.Seconds) //转换成标准时间
	asset.Time = ts
	res := BaseSerializeSave(stub, key, asset)
	return res
}

//保存钱包
func (t *TIDChaincode) saveWallet(stub shim.ChaincodeStubInterface, wallet Wallet) pb.Response {
	amount, err2 := strconv.Atoi(wallet.Amount)
	if err2 != nil {
		return IError("Amount failed to trans to int", wallet.Amount)
	}
	key := GenKey(wallet.Owner, wallet.AssetOwner, wallet.AssetCode)
	dataBytes, err := stub.GetState(key)
	if err != nil {
		return IError("Failed to get State", key)
	}
	if dataBytes != nil { //若存在资产，则新发行数量要加上已有数量
		existWallet := Wallet{}
		err = json.Unmarshal(dataBytes, &existWallet)
		if err != nil {
			return IError("Data failed to Unmarshal Wallet", string(dataBytes))
		}
		existAmount, err1 := strconv.Atoi(existWallet.Amount)
		if err1 != nil {
			return IError("Amount failed to trans to int", existWallet.Amount)
		}
		amount = existAmount + amount
	}
	wallet.Amount = strconv.Itoa(amount)
	if amount < 0 {
		return IError("Amount is Less than 0", wallet.Amount)
	}
	wallet.Owner = GetSHA256HashCodeInString(wallet.Owner)
	wallet.AssetOwner = GetSHA256HashCodeInString(wallet.AssetOwner)
	wallet.Txid = stub.GetTxID()                       //交易ID
	timestamp, _ := stub.GetTxTimestamp()              //交易时间戳
	ts := TimeSecondToString24(timestamp.Seconds) //转换成标准时间
	wallet.Time = ts
	res := BaseSerializeSave(stub, key, wallet)
	return res
}

//身份密钥（公钥）批量保存
func (t *TIDChaincode) saveKeys(stub shim.ChaincodeStubInterface, keys []InputKey) pb.Response {
	for i := 0; i < len(keys); i++ {
		res := t.checkKey(stub, keys[i],true)
		if res.Status != shim.OK {
			return res
		}
		res = t.saveKey(stub, keys[i].Key)
		if res.Status != shim.OK {
			return res
		}
		//保存行为
		action := Action{}
		action.Operator = keys[i].Auth.Id
		action.Module = MODULE_KEY
		action.Type = TYPE_SAVE
		action.Object = GenKey(keys[i].Key.Id)
		res = t.saveAction(stub,action)
		if res.Status != shim.OK {
			return res
		}
	}
	return ISuccess("Success save all Keys", "")
}

//身份密钥（公钥）保存
func (t *TIDChaincode) saveKey(stub shim.ChaincodeStubInterface, key Key) pb.Response {
	stateKey := GetSHA256HashCodeInString(key.PublickKey)
	key.Id = GetSHA256HashCodeInString(key.Id)
	key.Txid = stub.GetTxID()                          //交易ID
	timestamp, _ := stub.GetTxTimestamp()              //交易时间戳
	ts := TimeSecondToString24(timestamp.Seconds) //转换成标准时间
	key.Time = ts
	return BaseSerializeSave(stub, stateKey, key)
}

//身份关联批量保存
func (t *TIDChaincode) saveRels(stub shim.ChaincodeStubInterface, rels []InputRel) pb.Response {
	var res pb.Response
	for i := 0; i < len(rels); i++ {
		res = t.checkRel(stub, rels[i],true)
		if res.Status != shim.OK {
			return res
		}
		res = t.saveRel(stub, rels[i].Rel)
		if res.Status != shim.OK {
			return res
		}
		//保存行为
		action := Action{}
		action.Operator = rels[i].Auth.Id
		action.Module = MODULE_REL
		action.Type = TYPE_SAVE
		action.Object = GenKey(rels[i].Rel.Id)
		res = t.saveAction(stub,action)
		if res.Status != shim.OK {
			return res
		}
	}
	return ISuccess("Success save all Rels", "")
}

//检查身份关联
func (t *TIDChaincode) checkRel(stub shim.ChaincodeStubInterface, inputRel InputRel, isVerify bool) pb.Response {
	rel := inputRel.Rel
	//1. 查询当前身份是否存在
	idTemp := rel.Id
	id := GenKey(rel.Id) //输入的身份id生成的组成信息，通常为编号
	res := BaseGetByKey(stub, id)
	if res.Status != shim.OK {
		return res
	}
	rel.Id = id
	//2. 查询关联身份是否存在
	relIdTemp := rel.RelId
	relId := GenKey(rel.RelId) //输入的身份id生成的组成信息，通常为编号
	res = BaseGetByKey(stub, relId)
	if res.Status != shim.OK {
		return res
	}
	rel.RelId = relId
	//3. 判断是否存在记录
	key := GetSHA256HashCodeInString(idTemp + relIdTemp)
	res = BaseNoExist(stub, key)
	if res.Status != shim.OK {
		return res
	}
	//4. 身份认证
	relBytes, err := json.Marshal(rel) //key对象序列化,验签对象
	if err != nil {
		return IError("Failed to Marshal json", string(relBytes))
	}
	auth := inputRel.Auth
	if isVerify {
		res = t.verifyAuth(stub, auth, relBytes)
		if res.GetStatus() != shim.OK {
			return res
		}
	}
	//5. 关联权限认证，即需要 {当前操作身份} 与 {身份关联双方} 的【身份关联】资产进行交易
	inputTransaction := InputTransaction{}
	transaction := inputTransaction.Transaction
	transaction.Code = ASSET_CODE_REL
	transaction.From = auth.Id //当前操作身份，或者说提交身份，可能与被关联身份是同一身份，也可能不是（就是代理身份关联）
	transaction.Amount = ASSET_TRANS_AMOUNT
	// 1) 与{被关联身份}资产交易
	transaction.To = idTemp //回收给被关联身份
	transaction.Id = idTemp //被关联身份
	inputTransaction.Transaction = transaction
	res = t.transAsset(stub, inputTransaction, false)
	if res.GetStatus() != shim.OK {
		return res
	}
	// 2) 与{关联身份}资产交易
	transaction.Id = auth.Id //当前操作身份，或者说提交身份，可能与被关联身份是同一身份，也可能不是（就是代理身份关联）
	transaction.To = relIdTemp //回收给关联身份
	inputTransaction.Transaction = transaction
	res = t.transAsset(stub, inputTransaction, false)
	if res.GetStatus() != shim.OK {
		return res
	}
	return ISuccess("Success check Rel, Rel can save","")
}

//身份关联保存,身份关联保存到持久化状态数据库
func (t *TIDChaincode) saveRel(stub shim.ChaincodeStubInterface, rel Rel) pb.Response {
	key := GenKey(rel.Id,rel.RelId)
	rel.Id = GenKey(rel.Id) //输入的身份id生成的组成信息，通常为编号
	rel.RelId = GenKey(rel.Id) //输入的身份id生成的组成信息，通常为编号
	rel.Alias = GetSHA256HashCodeInString(rel.Alias) //加密存储
	rel.Txid = stub.GetTxID()                             //交易ID
	timestamp, _ := stub.GetTxTimestamp()                 //交易时间戳
	ts := TimeSecondToString24(timestamp.Seconds)    //转换成标准时间
	rel.Time = ts
	return BaseSerializeSave(stub, key, rel)
}

//行为保存
func (t *TIDChaincode) saveAction(stub shim.ChaincodeStubInterface, action Action) pb.Response {
	action.Txid = stub.GetTxID()                             //交易ID
	timestamp, _ := stub.GetTxTimestamp()                 //交易时间戳
	ts := TimeSecondToString24(timestamp.Seconds)    //转换成标准时间
	action.Time = ts
	key := GenKey(action.Operator, action.Module, action.Type, action.Object, action.Time)
	action.Operator = GetSHA256HashCodeInString(action.Operator) //加密存储
	action.Object = GetSHA256HashCodeInString(action.Object) //加密存储
	return BaseSerializeSave(stub, key, action)
}

//根据公钥读取密钥
func (t *TIDChaincode) getKey(stub shim.ChaincodeStubInterface, publicKey string) (Key, pb.Response) {
	key := Key{}
	keyBytes, err := stub.GetState(publicKey)
	if err != nil {
		return key, IError("Failed to get State", string(keyBytes))
	}
	if keyBytes == nil {
		return key, IMessage(NOEXIST, "Not Find Data", string(keyBytes))
	}
	err = json.Unmarshal(keyBytes, &key)
	if err != nil {
		return key, IError("Failed to Marshal json", string(keyBytes))
	}
	return key, ISuccess("Success", string(keyBytes))
}

//根据内容读取密钥
func (t *TIDChaincode) getKeyByValue(stub shim.ChaincodeStubInterface, name string, value string) (Key, error) {
	key := Key{}
	results, err := BaseQueryByValue(stub, name, value)
	if err != nil {
		return key, err
	}
	err = json.Unmarshal(results[0].Value, &key)
	if err != nil {
		return key, err
	}
	return key, nil
}

//身份密钥（公钥）检查
func (t *TIDChaincode) checkKey(stub shim.ChaincodeStubInterface, inputKey InputKey, isVerify bool) pb.Response {
	key := inputKey.Key
	//1. 检查公钥是否存在,存在则提示错误
	res := BaseNoExist(stub, key.PublickKey)
	if res.GetStatus() != shim.OK {
		return res
	}
	//2. 检查身份是否存在,不存在则报错
	tidKey := GenKey(key.Id) //输入的身份id生成的组成信息，通常为编号
	res = BaseGetByKey(stub, tidKey)
	if res.GetStatus() != shim.OK {
		return res
	}
	//特别说明：以上两点检查由于效率高因此优先检查；后两点检查则使用密码算法效率较差，因此放后。
	//3. 自验本次上传公钥有效性
	keyBytes, err := json.Marshal(key) //key对象序列化,验签对象
	if err != nil {
		return IError("Failed to Marshal json", string(keyBytes))
	}
	err = VerifyBase64(keyBytes, inputKey.Sign, key.PublickKey, inputKey.HashAlgorithm, key.Algorithm)
	if err != nil {
		return IError("Verify Failed", string(keyBytes))
	}
	//4. 检查是否公钥所属身份授权本次操作，若身份有强密钥则使用公钥验签，若没有强公钥则使用弱密钥使用密码验证
	auth := inputKey.Auth
	if isVerify {
		res = t.verifyAuth(stub, auth, keyBytes)
		if res.GetStatus() != shim.OK {
			return res
		}
	}
	//5. 密钥上传权限认证，即需要 {当前操作身份} 与 {key所属身份} 的【密钥上传】资产进行交易
	inputTransaction := InputTransaction{}
	transaction := inputTransaction.Transaction
	transaction.Code = ASSET_CODE_KEY
	transaction.From = auth.Id //当前操作身份，或者说提交身份，可能与key所属身份是同一身份，也可能不是（就是代理身份上传公钥）
	transaction.Amount = ASSET_TRANS_AMOUNT
	transaction.To = key.Id //回收给key所属身份
	transaction.Id = key.Id //key所属身份
	inputTransaction.Transaction = transaction
	res = t.transAsset(stub, inputTransaction, false)
	if res.GetStatus() != shim.OK {
		return res
	}
	return res
}

//身份授权验证--密码和密钥双重验证
func (t *TIDChaincode) verifyAuth(stub shim.ChaincodeStubInterface, auth Auth, dataBytes []byte) pb.Response {
	//强验证
	res := t.verifyAuthKey(stub, auth.Id, dataBytes, auth.Sign, auth.PublicKey, auth.HashAlgorithm)
	//弱验证，强验证不通过则进入，必须存在密码，只要验证通过就能保存此密钥
	if res.GetStatus() != shim.OK {
		res = t.verifyAuthPassword(stub, auth.Id, auth.Password)
		if res.GetStatus() != shim.OK {
			return res
		}
	}
	return ISuccess("Success to verify Auth", string(dataBytes))
}

//身份授权验证--密码验证
func (t *TIDChaincode) verifyAuthPassword(stub shim.ChaincodeStubInterface, id string, password string) pb.Response {
	key := GetSHA256HashCodeInString(id) //输入的身份id生成的组成信息，通常为编号
	tid, err1 := t.getTID(stub, key)
	if err1 != nil {
		return IError("Failed to get TID", id)
	}
	password = GetSHA256HashCodeInString(password)
	if !strings.EqualFold(password, tid.Password) {
		return IError("Failed to verify password", password)
	}
	return ISuccess("Success to verify password", password)
}

//身份授权验证--密钥验证
func (t *TIDChaincode) verifyAuthKey(stub shim.ChaincodeStubInterface, id string, dataBytes []byte,
	sign string, publicKey string, hashAlgorithm string) pb.Response {
	key, res := t.getKey(stub, publicKey)
	if res.Status != shim.OK {
		return res
	}
	err := VerifyBase64(dataBytes, sign, publicKey, hashAlgorithm, key.Algorithm)
	if err != nil {
		return IError("Failed to verify key", string(dataBytes))
	}
	return ISuccess("Success to verify key", string(dataBytes))
}

//读取身份
func (t *TIDChaincode) getTID(stub shim.ChaincodeStubInterface, key string) (TID, error) {
	tid := TID{}
	tidByte, err := stub.GetState(key)
	if err != nil {
		return tid, err
	}
	err = json.Unmarshal(tidByte, &tid)
	if err != nil {
		return tid, err
	}
	return tid, nil
}

//主执行方法
func main() {
	err := shim.Start(new(TIDChaincode))
	if err != nil {
		Error.Printf("Error starting chaincode: %s", err)
	}
}

/****************** 公共实现  ***************************/
//===========  统一返回消息处理  =================
//返回信息对象
type Result struct {
	Code string `json:"code"` //自定义编码
	Msg  string `json:"msg"`  //消息
	Data string `json:"data"` //数据JSON对象
}
//成功返回
func ISuccess(msg string, data string) pb.Response {
	t := &Result{"", "", ""}
	t.Code = SUCCESS
	t.Msg = msg
	t.Data = data
	resJson, err := json.Marshal(t)
	if err != nil {
		return shim.Error("Failed to generate json result " + err.Error())
	}
	return shim.Success(resJson)
}
//零值成功返回
func ISuccessNil() pb.Response {
	return shim.Success(nil)
}
//默认错误返回
func IError(msg string, data string) pb.Response {
	t := &Result{"", "", ""}
	t.Code = ERROR
	t.Msg = msg
	t.Data = data
	resJson, err := json.Marshal(t)
	if err != nil {
		return shim.Error("Failed to generate json result " + err.Error())
	}
	return shim.Error(string(resJson))
}
//自定义错误返回
func IMessage(code string, msg string, data string) pb.Response {
	t := &Result{"", "", ""}
	t.Code = code
	t.Msg = msg
	t.Data = data
	resJson, err := json.Marshal(t)
	if err != nil {
		return shim.Error("Failed to generate json result " + err.Error())
	}
	return shim.Error(string(resJson))
}

//============== 统一时间处理  ===================================
/**
 * 转换成标准时间字符串
 * sec 时间戳，即时间秒数
 * layout 标准格式，如2006-01-02 03:04:05 PM为12小时制，2006-01-02 15:04:05为24小时制
 */
func TimeSecondToString(sec int64, layout string) string {
	tm := time.Unix(sec, 0)
	ts := tm.Format(layout)
	return ts
}

func TimeSecondToString24(sec int64) string {
	return TimeSecondToString(sec, "2006-01-02 15:04:05")
}

func TimeSecondToString12(sec int64) string {
	return TimeSecondToString(sec, "2006-01-02 03:04:05 PM")
}

//============== 统一算法处理  ===================================
//生成key,把输入字符数组组成字符串作为生成key的原字符串
func GenKey(args ...string) string {
	key := GetSHA256HashCodeInString(strings.Join(args,""))
	return key
}

//========= Hash算法 ==============
//SHA256生成哈希值
func GetSHA256HashCodeInString(message string) string {
	messageByte := []byte(message)
	return GetSHA256HashCode(messageByte)
}

//SHA256生成哈希值
func GetSHA256HashCode(message []byte) string {
	//方法一：
	/*	hash := sha256.New()  //创建一个基于SHA256算法的hash.Hash接口的对象
		hash.Write(message)   //输入数据
		bytes := hash.Sum(nil)  //计算哈希值
		hashCode := hex.EncodeToString(bytes)  //将字符串编码为16进制格式,返回字符串
		//方法二：
		//bytes:=sha256.Sum256(message)//计算哈希值，返回一个长度为32的数组
		//hashcode:=hex.EncodeToString(bytes[:])//将数组转换成切片，转换成16进制，返回字符串*/
	//返回哈希值
	return GetHashCodeString(message, crypto.SHA256)
}

/**
 * 根据国际算法名称生成哈希值
 */
func GetHashCodeStringByNameInString(message string, hashName string) string {
	return GetHashCodeStringInString(message, GetHash(hashName))
}

/**
 * 根据国际算法名称生成哈希值
 */
func GetHashCodeByNameInString(message string, hashName string) []byte {
	return GetHashCodeInString(message, GetHash(hashName))
}

/**
 * 根据国际算法名称生成哈希值
 */
func GetHashCodeStringInString(message string, hash crypto.Hash) string {
	messageByte := []byte(message)
	return GetHashCodeString(messageByte, hash)
}

/**
 * 根据国际算法名称生成哈希值
 */
func GetHashCodeInString(message string, hash crypto.Hash) []byte {
	messageByte := []byte(message)
	return GetHashCode(messageByte, hash)
}

/**
 * 根据国际算法名称生成哈希值
 */
func GetHashCodeStringByName(message []byte, hashName string) string {
	return GetHashCodeString(message, GetHash(hashName))
}

/**
 * 根据国际算法名称生成哈希值
 */
func GetHashCodeByName(message []byte, hashName string) []byte {
	return GetHashCode(message, GetHash(hashName))
}

/**
 * 根据国际算法标准类型生成哈希值
 */
func GetHashCodeString(message []byte, hash crypto.Hash) string {
	hashCode := GetHashCode(message, hash)      //计算哈希值
	hashCodeStr := hex.EncodeToString(hashCode) //将字符串编码为16进制格式,返回字符串
	return hashCodeStr
}

/**
 * 根据国际算法标准类型生成哈希值
 */
func GetHashCode(message []byte, hash crypto.Hash) []byte {
	//方法一：
	hashInstance := hash.New()        //创建一个基于SHA256算法的hash.Hash接口的对象
	hashInstance.Write(message)       //输入数据
	hashCode := hashInstance.Sum(nil) //计算哈希值
	//hashCodeStr := hex.EncodeToString(bytes)  //将字符串编码为16进制格式,返回字符串
	//方法二：
	//hashCode:=sha256.Sum256(message)//计算哈希值，返回一个长度为32的数组
	//hashcodeStr:=hex.EncodeToString(bytes[:])//将数组转换成切片，转换成16进制，返回字符串
	//返回哈希值
	return hashCode
}

/** 转换Hash类型
 * 根据字符类型转换成crypto.Hash标准类型
 */
func GetHash(hashName string) crypto.Hash {
	if strings.EqualFold(hashName, MD5) {
		return crypto.MD5
	}
	if strings.EqualFold(hashName, SHA256) {
		return crypto.SHA256
	}
	if strings.EqualFold(hashName, SHA512) {
		return crypto.SHA512
	}
	return crypto.MD4
}
//=========== 统一非对称加密算法DSA ========
//私钥签名,默认为RSA算法
func Sign(data []byte, privateKeyStr string, hashName string, dsaName string) ([]byte, error) {
	if strings.EqualFold(dsaName, "ecdsa") {
	}
	if strings.EqualFold(dsaName, "sm2") {
	}
	return RSASign(data, privateKeyStr, hashName)
}

//公钥验签,默认为RSA算法
func Verify(data []byte, sign []byte, publicKeyStr string, hashName string, dsaName string) error {
	if strings.EqualFold(dsaName, "ecdsa") {
	}
	if strings.EqualFold(dsaName, "sm2") {
	}
	return RSAVerify(data, sign, publicKeyStr, hashName)
}

//Base64私钥签名,默认为RSA算法，签名值编码成base64字符返回
func SignBase64(data []byte, privateKeyStr string, hashName string, dsaName string) (string, error) {
	if strings.EqualFold(dsaName, "ecdsa") {
	}
	if strings.EqualFold(dsaName, "sm2") {
	}
	return RSASignBase64(data, privateKeyStr, hashName)
}

//Base64公钥验签,默认为RSA算法,参数sign签名值是由base64编码字符串
func VerifyBase64(data []byte, sign string, publicKeyStr string, hashName string, dsaName string) error {
	if strings.EqualFold(dsaName, "ecdsa") {
	}
	if strings.EqualFold(dsaName, "sm2") {
	}
	return RSAVerifyBase64(data, sign, publicKeyStr, hashName)
}
//=============== RSA 算法==============
//私钥签名
func RSASign(data []byte, privateKeyStr string, hashName string) ([]byte, error) {
	// 1、选择hash算法，对需要签名的数据进行hash运算
	hash := GetHash(hashName)
	hashCode := GetHashCode(data, hash)
	// 2、解析私钥对象
	privateKey, err := RSAParsePrivateKeyInString(privateKeyStr)
	if err != nil {
		return nil, err
	}
	// 3、RSA数字签名（参数是随机数、私钥对象、哈希类型、签名文件的哈希串，生成bash64编码）
	bytes, err := rsa.SignPKCS1v15(rand.Reader, privateKey, hash, hashCode)
	if err != nil {
		return nil, err
	}
	return bytes, nil
}

//公钥验签
func RSAVerify(data []byte, sign []byte, publicKeyStr string, hashName string) error {
	// 2、选择hash算法，对需要签名的数据进行hash运算
	hash := GetHash(hashName)
	hashCode := GetHashCode(data, hash)
	// 3、读取公钥文件，解析出公钥对象
	publicKey, err := RSAParsePublicKeyInString(publicKeyStr)
	if err != nil {
		return err
	}
	// 4、RSA验证数字签名（参数是公钥对象、哈希类型、签名文件的哈希串、签名后的字节）
	return rsa.VerifyPKCS1v15(publicKey, hash, hashCode, sign)
}

//base64私钥签名，签名后封装成base64字符串返回
func RSASignBase64(data []byte, privateKeyStr string, hashName string) (string, error) {
	bytes, err := RSASign(data, privateKeyStr, hashName)
	if err != nil {
		return "", err
	}
	return base64.StdEncoding.EncodeToString(bytes), nil
}

//base64公钥验签，参数sign签名值是由base64编码字符串
func RSAVerifyBase64(data []byte, sign string, publicKeyStr string, hashName string) error {
	// 1、对base64编码的签名内容进行解码，返回签名字节
	signBytes, err := base64.StdEncoding.DecodeString(sign)
	if err != nil {
		return err
	}
	// 4、RSA验证数字签名（参数是公钥对象、哈希类型、签名文件的哈希串、签名后的字节）
	return RSAVerify(data, signBytes, publicKeyStr, hashName)
}

//公钥文件解析公钥对象
func RSAReadParsePublicKey(filename string) (*rsa.PublicKey, error) {
	// 1、读取公钥文件，获取公钥字节
	publicKeyBytes, err := ioutil.ReadFile(filename)
	if err != nil {
		return nil, err
	}
	// 2、解析公钥对象
	return RSAParsePublicKey(publicKeyBytes)
}

//私钥文件解析出私钥对象
func RSAReadParsePrivateKey(filename string) (*rsa.PrivateKey, error) {
	// 1、读取私钥文件，获取私钥字节
	privateKeyBytes, err := ioutil.ReadFile(filename)
	if err != nil {
		return nil, err
	}
	// 2、解析私钥对象
	return RSAParsePrivateKey(privateKeyBytes)
}

//解析公钥对象
func RSAParsePublicKeyInString(publicKeyStr string) (*rsa.PublicKey, error) {
	publicKeyBytes := []byte(publicKeyStr)
	return RSAParsePublicKey(publicKeyBytes)
}

//解析私钥对象
func RSAParsePrivateKeyInString(privateKeyStr string) (*rsa.PrivateKey, error) {
	privateKeyBytes := []byte(privateKeyStr)
	return RSAParsePrivateKey(privateKeyBytes)
}

//解析公钥对象
func RSAParsePublicKey(publicKeyBytes []byte) (*rsa.PublicKey, error) {
	// 1、解码公钥字节，生成加密对象
	block, _ := pem.Decode(publicKeyBytes)
	if block == nil {
		return nil, errors.New("公钥信息错误！")
	}
	// 2、解析DER编码的公钥，生成公钥接口
	publicKeyInterface, err := x509.ParsePKIXPublicKey(block.Bytes)
	if err != nil {
		return nil, err
	}
	// 4、公钥接口转型成公钥对象
	publicKey := publicKeyInterface.(*rsa.PublicKey)
	return publicKey, nil
}

//解析私钥对象
func RSAParsePrivateKey(privateKeyBytes []byte) (*rsa.PrivateKey, error) {
	// 1、解码私钥字节，生成加密对象
	block, _ := pem.Decode(privateKeyBytes)
	if block == nil {
		return nil, errors.New("私钥信息错误！")
	}
	// 2、解析DER编码的私钥，生成私钥对象
	privateKey, err := x509.ParsePKCS1PrivateKey(block.Bytes)
	if err != nil {
		return nil, err
	}
	return privateKey, nil
}

//===============  统一数据处理 ================
//KV结构
type KV struct {
	Key   string `json:"key"`   //key
	Value []byte `json:"value"` //value,任意对象类型
}

//key加密后序列化保存，通过原值生成key并把实体对象序列化进行存储
func BaseSerializeSaves(stub shim.ChaincodeStubInterface, key string, entity interface{}) pb.Response {
	key = GetSHA256HashCodeInString(key)
	return BaseSerializeSave(stub, key, entity)
}

//序列化保存，把实体对象序列化进行存储
func BaseSerializeSave(stub shim.ChaincodeStubInterface, key string, entity interface{}) pb.Response {
	data, err := json.Marshal(entity) //序列化
	if err != nil {
		return IError("Failed to Marshal json", key)
	}
	return BaseSave(stub, key, data)
}

//key加密后保存
func BaseSaves(stub shim.ChaincodeStubInterface, key string, data []byte) pb.Response {
	key = GetSHA256HashCodeInString(key)
	return BaseSave(stub, key, data)
}

//保存
func BaseSave(stub shim.ChaincodeStubInterface, key string, data []byte) pb.Response {
	// Write the state back to the ledger
	err := stub.PutState(key, data) //保存,key不存在则新增，存在则更新
	if err != nil {
		return IError("Failed to save Data", string(data))
	}
	return ISuccess("Success", string(data))
}

//删除
func BaseDelete(stub shim.ChaincodeStubInterface, id string) pb.Response {
	key := GenKey(id)
	return BaseDeleteByKey(stub, key)
}

//删除
func BaseDeleteByKey(stub shim.ChaincodeStubInterface, key string) pb.Response {
	//判断是否存在记录
	res := BaseGetByKey(stub, key)
	if res.GetStatus() != shim.OK {
		return res
	}
	// Delete the key from the state in ledger
	err := stub.DelState(key)
	if err != nil {
		return IError("Failed to delete", key)
	}
	return ISuccess("Success", key)
}

//key加密后获取
func BaseGet(stub shim.ChaincodeStubInterface, id string) pb.Response {
	key := GenKey(id)
	return BaseGetByKey(stub, key)
}

//获取
func BaseGetByKey(stub shim.ChaincodeStubInterface, key string) pb.Response {
	// Get the state from the ledger
	obj, err := stub.GetState(key)
	if err != nil {
		return IError("Failed to get State", key)
	}
	if obj == nil {
		return IMessage(NOEXIST, "Not Find Data", key)
	}
	return ISuccess("Success", string(obj))
}

//判断不存在
func BaseNoExist(stub shim.ChaincodeStubInterface, key string) pb.Response {
	// Get the state from the ledger
	obj, err := stub.GetState(key)
	if err != nil {
		return IError("Failed to get State", key)
	}
	if obj != nil {
		return IMessage(EXIST, "Data Exits,can`t save!", key)
	}
	return ISuccess("Success", "")
}

//富查询，目前只有Couchdb支持
func BaseQueryByValue(stub shim.ChaincodeStubInterface, key string, value string) ([]KV, error) {
	queryString := fmt.Sprintf("{\"selector\":{\"%s\":\"%s\"}}", key, value)
	return BaseQuery(stub, queryString)
}

//富查询，目前只有Couchdb支持
func BaseQuery(stub shim.ChaincodeStubInterface, query string) ([]KV, error) {
	// Get the state from the ledger
	resultsIterator, err := stub.GetQueryResult(query)
	if err != nil {
		return nil, err
	}
	var results []KV
	var result = KV{}
	for resultsIterator.HasNext() {
		res, err1 := resultsIterator.Next()
		if err1 != nil {
			return nil, err1
		}
		result.Key = res.Key
		result.Value = res.Value
		results = append(results, result)
	}
	return results, nil
}

