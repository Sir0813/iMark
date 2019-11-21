package com.dm.user.util;

import com.ahdms.asn1.encrypt.EnvelopedData;
import com.ahdms.exception.*;
import com.ahdms.sv.SVTool;
import com.ahdms.util.AsnUtil;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.util.encoders.Base64;

import java.io.UnsupportedEncodingException;

/**
 * @author cui
 * @date 2019/11/19
 * @hour 14
 */
public class IkiUtil {

    /**
     * 数字签名
     *
     * @param singString 需要签名原文
     * @return
     * @throws SVS_ServerConnectException
     * @throws SVS_PrivatekeyAccessRightException
     * @throws SVS_SignDataException
     * @throws SVS_InvalidParameterException
     * @throws UnsupportedEncodingException
     */
    public static String signData(String singString) throws SVS_ServerConnectException, SVS_PrivatekeyAccessRightException, SVS_SignDataException, SVS_InvalidParameterException, UnsupportedEncodingException {
        SVTool tool = SVTool.getInstance();
        tool.SVS_InitServerConnect("121.40.140.67");
        byte[] signBytes = tool.SVS_SignData(9, "dms123456", singString.getBytes());
        byte[] bytes = Base64.encode(signBytes);
        String signData = new String(bytes, "UTF-8");
        return signData;
    }

    /**
     * 数字验签
     *
     * @param aid       客户端aid可信标识
     * @param inData    签名原文
     * @param signature 签名值
     * @return
     * @throws SVS_ServerConnectException
     * @throws SVS_SignatureEncodeException
     * @throws SVS_CertExpiredException
     * @throws SVS_NotFoundPKMException
     * @throws SVS_CertException
     * @throws SVS_SignatureException
     * @throws SVS_VerifyDataException
     * @throws SVS_InvalidParameterException
     * @throws SVS_CheckIRLException
     * @throws SVS_CertNotTrustException
     * @throws SVS_CertIneffectiveException
     * @throws SVS_CertTypeException
     * @throws SVS_CertCancelException
     * @throws SVS_GetIRLException
     */
    public static boolean verifyData(String aid, String inData, String signature) throws SVS_ServerConnectException, SVS_SignatureEncodeException, SVS_CertExpiredException, SVS_NotFoundPKMException, SVS_CertException, SVS_SignatureException, SVS_VerifyDataException, SVS_InvalidParameterException, SVS_CheckIRLException, SVS_CertNotTrustException, SVS_CertIneffectiveException, SVS_CertTypeException, SVS_CertCancelException, SVS_GetIRLException {
        byte[] bytes1 = Base64.decode(aid);
        ASN1Sequence asn1Encodables = AsnUtil.byteToASN1Sequence(bytes1);
        Certificate cert = Certificate.getInstance(asn1Encodables);
        SVTool tool = SVTool.getInstance();
        tool.SVS_InitServerConnect("121.40.140.67");
        byte[] bytes = Base64.decode(signature);
        boolean b = tool.SVS_VerifyData(cert, inData.getBytes(), bytes, 1);
        return b;
    }

    /**
     * 封装数字信封接口
     *
     * @param aid    客户端可信标识 aid
     * @param inData 被封装数据
     * @return
     * @throws SVS_ServerConnectException
     * @throws SVS_CertTypeException
     * @throws SVS_EncryptEnvelopeException
     * @throws SVS_InvalidParameterException
     */
    public static String encryptEnvelope(String aid, String inData) throws SVS_ServerConnectException, SVS_CertTypeException, SVS_EncryptEnvelopeException, SVS_InvalidParameterException {
        byte[] bytes = Base64.decode(aid);
        ASN1Sequence asn1Encodables = AsnUtil.byteToASN1Sequence(bytes);
        Certificate cert = Certificate.getInstance(asn1Encodables);
        SVTool tool = SVTool.getInstance();
        tool.SVS_InitServerConnect("121.40.140.67");
        byte[] encryptBytes = tool.SVS_EncryptEnvelope(cert, inData.getBytes());
        EnvelopedData envelopedData = EnvelopedData.getInstance(AsnUtil.byteToASN1Sequence(encryptBytes));
        ContentInfo contentInfo2 = new ContentInfo(new ASN1ObjectIdentifier("1.2.156.10197.6.1.4.2.3"), envelopedData);
        byte[] result = AsnUtil.asn1ToByte(contentInfo2);
        String resultBase64 = Base64.toBase64String(result);
        return resultBase64;
    }

    /**
     * 解封数字信封
     *
     * @param envelopeData 数字信封base64之后的值
     * @return
     * @throws SVS_ServerConnectException
     * @throws SVS_DecryptEnvelopeException
     * @throws SVS_PrivatekeyAccessRightException
     * @throws SVS_InvalidParameterException
     * @throws SVS_EnvelopeEncodeException
     */
    public static String decryptEnvelope(String envelopeData) throws SVS_ServerConnectException, SVS_DecryptEnvelopeException, SVS_PrivatekeyAccessRightException, SVS_InvalidParameterException, SVS_EnvelopeEncodeException {
        SVTool tool = SVTool.getInstance();
        tool.SVS_InitServerConnect("121.40.140.67");
        byte[] decode = Base64.decode(envelopeData.getBytes());
        ContentInfo contentInfo = ContentInfo.getInstance(AsnUtil.byteToASN1Sequence(decode));
        EnvelopedData envelopedData = EnvelopedData.getInstance(contentInfo.getContent());
        byte[] decryptBytes = tool.SVS_DecryptEnvelope(AsnUtil.asn1ToByte(envelopedData), 9, "dms123456");
        return new String(decryptBytes);
    }

}
