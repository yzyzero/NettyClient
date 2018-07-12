package com.xyd.transfer.ip;

import io.netty.buffer.ByteBuf;


/**
 * 序号 	语法 			长度（字节） 		编码规则
 * 1 	结果代码 		1 				0 标识成功，其他参见错误代码列表。
 * 2 	结果描述长度 		2				结果描述的长度；如果无结果描述则长度为0。
 * 3 	结果描述内容 		结果描述长度 		可选，对应答结果进行描述。
 */
public class ResponsePack extends SendPack {

	private OperationType operation;
	private Error code;
	private String description;

	public ResponsePack(RawPack pack) {
		this(pack.getSessionID(), pack.getSource(), pack.getTargets(), pack.getOperation(), pack.getBuf());
	}
	
	public ResponsePack(int sessionID, String source, String[] targets, OperationType operation) {
		this.setSessionID(sessionID);
		this.setSource(source);
		this.setTargets(targets);
		
		this.operation = operation;
	}
	
	public ResponsePack(int sessionID, String source, String[] targets, OperationType operation, ByteBuf frame) {
		this(sessionID, source, targets, operation);
		
		code = Error.valueOf(frame.readByte());
		int descLen = frame.readShort();
		byte bDesc[] = new byte[descLen];
		frame.readBytes(bDesc);
		description = new String(bDesc);
	}

	@Override
	protected int generateBuf(ByteBuf buf) {
		buf.writeByte(code.getCode());
		if(description == null || description.equals("")) {
			buf.writeShort(0);
			return 3;
		} else {
			byte[] bDesc = description.getBytes();
			buf.writeShort(bDesc.length);
			buf.writeBytes(bDesc);
			return bDesc.length + 3;
		}
	}

	public Error getCode() {
		return code;
	}

	public void setCode(Error code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public PackType getType() {
		return PackType.RESPONSE;
	}

	@Override
	public OperationType getOperation() {
		return operation;
	}
}
