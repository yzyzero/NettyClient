package com.xyd.transfer.ip.datapack;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.DecoderException;

import com.xyd.transfer.ip.OperationType;
import com.xyd.transfer.ip.PackType;
import com.xyd.transfer.ip.SendPack;
import com.xyd.transfer.ip.parameter.IPParameter;

import io.netty.buffer.ByteBuf;

public class ResponseStatusQuery extends SendPack {
	private Set<IPParameter> parameters = new HashSet<>();
	
	public ResponseStatusQuery(int sessionID, String source, Set<IPParameter> parameters) {
		this.setSessionID(sessionID);
		this.setSource(source);
		this.parameters = parameters;
	}
	
	public ResponseStatusQuery(int sessionID, String source, IPParameter parameter) {
		this.setSessionID(sessionID);
		this.setSource(source);
		this.parameters.add(parameter);
	}

	@Override
	protected int generateBuf(ByteBuf buf) throws Exception {
		int contentLen = 4;
		buf.writeByte(0);//结果代码
		buf.writeShort(0);//结果描述长度
		buf.writeByte(parameters.size());//查询参数个数
		for (IPParameter param : parameters) {
			contentLen += param.writeBytes(buf);
		}
		return contentLen;
	}

	public void setParameters(Set<IPParameter> parameters) {
		this.parameters = parameters;
	}

	@Override
	public OperationType getOperation() {
		return OperationType.TERMINAL_STATUS_QUERY;
	}
	
	@Override
	protected PackType getType() {
		return PackType.RESPONSE;
	}
}
