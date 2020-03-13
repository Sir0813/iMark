/*
* 区块链存证
*/

package main

import (
    //"bytes"
    "encoding/json"
	"fmt"
	"log"
	"os"
	//"strconv"

	"github.com/hyperledger/fabric/core/chaincode/shim"
	pb "github.com/hyperledger/fabric/protos/peer"
)

var Info  = log.New(os.Stdout, "INFO: ", log.Ldate|log.Ltime|log.Lshortfile)
var Error = log.New(os.Stderr, "ERROR: ", log.Ldate|log.Ltime|log.Lshortfile)

// CIDChaincode example simple Chaincode implementation
type CIDChaincode struct {
}

//存证对象
type CID struct {     //Go struct处理json格式，[`]与普通单引号[']不一样，则由键盘“~”输入，务必注意否则编译不了
    Id      string   `json:"id"`   //存证ID，即存证内容所生成指纹
    Name    string   `json:"name"` //存证姓名
    Time    string   `json:"time"` //存证时间
    Data    string   `json:"data"` //扩展数据，可选，可以是JSON字符内容
    //Description string `json:"description"`   //存证描述
    //UserId    string `json:"userId"` //存证提交人ID
    //Source    string `json:"source"` //存证来源
}

//返回信息对象
type ResInfo struct {
    Code   string   `json:"code"`  //编码
    Msg    string   `json:"msg"`   //消息
    Data   string   `json:"data"`  //数据JSON对象
}

func (t *ResInfo) error(msg string, data string) {
    t.Code = "100"
    t.Msg = msg
    t.Data = data
}
func (t *ResInfo) ok(msg string, data string) {
    t.Code = "200"
    t.Msg = msg
    t.Data = data
}

func (t *ResInfo) response() pb.Response {
    resJson, err := json.Marshal(t)
    if err != nil {
        return shim.Error("Failed to generate json result " + err.Error())
    }

    return shim.Error(string(resJson))
}

func (t *ResInfo) responseSuccess() pb.Response {
    resJson, err := json.Marshal(t)
    if err != nil {
        return shim.Error("Failed to generate json result " + err.Error())
    }
    return shim.Success(resJson)
}

// Init initializes the chaincode state
//简化处理，暂无初始化，默认为空，否则合约初始化或升级时需要模拟初始化就会报错
func (t *CIDChaincode) Init(stub shim.ChaincodeStubInterface) pb.Response {
	Info.Println("########### CID Init ###########")
	return shim.Success(nil)
}

// Invoke makes payment of X units from A to B
func (t *CIDChaincode) Invoke(stub shim.ChaincodeStubInterface) pb.Response {
	Info.Println("########### CID Invoke ###########")

	function, args := stub.GetFunctionAndParameters()

	if function == "delete" {
		// Deletes an entity from its state
		return t.delete(stub, args)
	}

	if function == "query" {
		// queries an entity state
		return t.query(stub, args)
	}

	if function == "save" {
		// Deletes an entity from its state
		return t.save(stub, args)
	}

	Error.Printf("Unknown action, check the first argument, must be one of `delete`, `query`, or `move`. But got: %v", args[0])
	return shim.Error(fmt.Sprintf("Unknown action, check the first argument, must be one of `delete`, `query`, or `move`. But got: %v", args[0]))
}

func (t *CIDChaincode) save(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    res := &ResInfo{"", "",""}
	// must be an invoke
	var id, name,time,data string    // Entities
	var err error

	if len(args) != 4 {
		res.error("Incorrect number of arguments. Expecting 4, function followed by [id,name,time,data]","");
		return res.response()
	}

	id = args[0]
	name = args[1]
	time = args[2]
	data = args[3]

	// Get the state from the ledger
	// TODO: will be nice to have a GetAllState call to ledger
	cidobj, err := stub.GetState(id)
	if err != nil {
		return shim.Error("Failed to get state")
		res.error("Failed to get state",id);
        return res.response()
	}
    if cidobj != nil {
    	res := &ResInfo{"101", "CID Exits,can`t save!",id}
    	return res.response()
    }

	cid := &CID{id, name, time, data}
    cidjson, err := json.Marshal(cid)    //序列化

	// Write the state back to the ledger
	err = stub.PutState(id, cidjson)
	if err != nil {
		res.error(err.Error(),string(cidjson));
        return res.response()
	}

    res.ok("Success",string(cidjson));
    return res.responseSuccess()
}

// Deletes an entity from state
func (t *CIDChaincode) delete(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    res := &ResInfo{"", "",""}

	if len(args) != 1 {
		res.error("Incorrect number of arguments. Expecting 1","")
        return res.response()
	}

	id := args[0]

	// Delete the key from the state in ledger
	err := stub.DelState(id)
	if err != nil {
		res.error("Failed to delete",id);
        return res.response()
	}

	res.ok("Success",id);
    return res.responseSuccess()
}

// Query callback representing the query of a chaincode
func (t *CIDChaincode) query(stub shim.ChaincodeStubInterface, args []string) pb.Response {
    res := &ResInfo{"", "",""}
	var id string // Entities
	var err error

	if len(args) != 1 {
		res.error("Incorrect number of arguments. Expecting name of the person to query","")
		return res.response()
	}

	id = args[0]

	// Get the state from the ledger
	cid, err := stub.GetState(id)
	if err != nil {
		res.error("Failed to query",id);
        return res.response()
	}

	if cid == nil {
		res := &ResInfo{"400", "Not Find Data",id}
        return res.response()
	}

	res.ok("Success",string(cid));
    return res.responseSuccess()
}

func main() {
	err := shim.Start(new(CIDChaincode))
	if err != nil {
		Error.Printf("Error starting chaincode: %s", err)
	}
}