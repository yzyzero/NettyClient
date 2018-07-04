package com.xyd.transfer.ip.datapack;

public abstract class BasePack {

	/* -----------------------------------------------------------------------------------
	消息头固定长度：12

	序号	语法	长度（字节）	编码规则
	1	包头标记		2	值为0xFEFD，用于快速判断数据是否合法。
	2	协议版本号	2	值为0x0100，当前协议采用的版本号。
	3	会话标识		4	请求数据包在发送端的统一编号，单向递增。请求与应答的会话标识要保持一致。
	4	数据包类型	1	1：请求数据包；2：应答数据包。
	5	签名标识		1	0：不签名； 1：验证数据包含对数据包消息体的数据签名。
	6	数据包长度	2	标识为整个应急广播数据包的长度。
	-------------------------------------------------------------------------------------*/
	private final short protocolVersion = 256;
	private int sessionID = 0;

	/* -----------------------------------------------------------------------------------
	序号	语法	       长度（字节）				编码规则
	1	数据源对象编码		 9						数据包发送端的资源编码。应急广播资源编码采用18位数字码：资源类型码（2个数字）+资源子类型码（2个数字）+地区编码（12个数字）+扩展码（2个数字），采用压缩性BCD编码，9字节。
	2	数据目标对象数量	 2						数据目标对象数量。
	3	数据目标对象编码序列 9*数据目标对象数量		数据包接收端的资源编码。目标对象编码格式同源对象编码。
	4	业务数据类型		 1						表示数据所包含的业务类型：参见CommandType
	5	业务数据长度		 2						业务数据内容长度。
	6	业务数据内容	业务数据长度				业务数据内容根据业务数据类型不同。
	----------------------------------------------------------------------------------- */
	private String source;
	private String targets[], oldTargets[];
	
	/* -----------------------------------------------------------------------------------
	序号        语法                	长度 （字节）        编码规则
	1             数字签名长度   	2             	16 位字段，用于指示数字签名时间、签名证书编号、数字签名的总长度。
	2             数字签名时间   	4 				数字签名UTC 时间。
	3 		签名证书编号		6 				签名验证需要使用的发送源数字证书编号，全国范围内采用统一的唯一编号，证书编号48 比特，采用BCD 码表示的12 个数字。
	4 		数字签名 		数字签名长度		数字签名数据包含应急广播数据包中消息头和消息体的数字签名信息。
	5 		CRC32 			4				数据包CRC32 值，计算范围为数据包所有数据。计算方法见附录A。
	----------------------------------------------------------------------------------- */
	private int signatureTime = 0;
	private String signatureNumber;
	private byte signature[];
	
	public int getSessionID() {
		return sessionID;
	}
	public String getSource() {
		return source;
	}
	
	public int getTargetNumber() {
		return targets.length;
	}
	
	public String[] getTargets() {
		return targets;
	}
	
	public String[] cloneTargets() {
		oldTargets = targets.clone();
		return oldTargets;
	}
	public void recoverTargets() {
		if(oldTargets != null && oldTargets.length >0) {
			targets = oldTargets;
			oldTargets = null;
		}
	}
	
	public boolean useSignature() {
		return signatureTime != 0;
	}

	public int getSignatureLength() {
		return signature.length;
	}
	
	public int getSignatureTime() {
		return signatureTime;
	}
	public String getSignatureNumber() {
		return signatureNumber;
	}
	public byte[] getSignature() {
		return signature;
	}
	public short getProtocolVersion() {
		return protocolVersion;
	}

	public abstract OperationType getOperation();

	public void setSource(String source) {
		this.source = source;
	}
	public void setTargets(String...targets) {
		this.targets = targets;
	}
	
	protected void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}
	protected void setSignatureTime(int signatureTime) {
		this.signatureTime = signatureTime;
	}
	protected void setSignatureNumber(String signatureNumber) {
		this.signatureNumber = signatureNumber;
	}
	protected void setSignature(byte[] signature) {
		this.signature = signature;
	}
}
