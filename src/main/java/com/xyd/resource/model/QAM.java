package com.xyd.resource.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;


@Embeddable
public class QAM implements Cloneable {
	@Column(name="singal_mode")
	@Enumerated(EnumType.ORDINAL)
	private SingalMode singalMode;	// 信号模式
	private double frequency;		// 调制频率
	@Enumerated(EnumType.ORDINAL)
	private Definition qam;			// QAM (16, 32, 64, 128, 256)
	private Integer bandwidth;		// 带宽
	private Integer fec;
	@Column(name="freq_flag")
	private Integer freqFlag;
	@Column(name="header_mode")
	private Integer headerMode;
	@Column(name="interleaving_mode")
	private Integer interleavingMode;
	private Integer modulation;
	private Integer subcarrier;

	@Transient
	private int occupiedBandwidth;//占用带宽
	
	public QAM() {}

	public SingalMode getSingalMode() {
		return singalMode;
	}

	public void setSingalMode(SingalMode singalMode) {
		this.singalMode = singalMode;
	}

	public double getFrequency() {
		return frequency;
	}

	public void setFrequency(double frequency) {
		this.frequency = frequency;
	}

	public Definition getQam() {
		return qam;
	}

	public void setQam(Definition qam) {
		this.qam = qam;
	}

	public Integer getBandwidth() {
		return this.bandwidth;
	}

	public void setBandwidth(Integer bandwidth) {
		this.bandwidth = bandwidth;
	}

	public Integer getFec() {
		return this.fec;
	}

	public void setFec(Integer fec) {
		this.fec = fec;
	}

	public Integer getFreqFlag() {
		if(this.freqFlag==null)
		{
			return 0;
		}
		return this.freqFlag;
	}

	public void setFreqFlag(Integer freqFlag) {
		this.freqFlag = freqFlag;
	}

	public Integer getHeaderMode() {
		if(this.headerMode==null)
		{
			return 0;
		}
		return this.headerMode;
	}

	public void setHeaderMode(Integer headerMode) {
		this.headerMode = headerMode;
	}

	public Integer getInterleavingMode() {
		if(this.interleavingMode==null)
		{
			return 0;
		}
		return this.interleavingMode;
	}

	public void setInterleavingMode(Integer interleavingMode) {
		this.interleavingMode = interleavingMode;
	}

	public Integer getModulation() {
		if(this.modulation==null)
		{
			return 0;
		}
		return this.modulation;
	}

	public void setModulation(Integer modulation) {
		this.modulation = modulation;
	}

	public Integer getSubcarrier() {
		if(this.subcarrier==null)
		{
			return 0;
		}
		return this.subcarrier;
	}

	public void setSubcarrier(Integer subcarrier) {
		this.subcarrier = subcarrier;
	}

	public int getOccupiedBandwidth() {
		return occupiedBandwidth;
	}
	public void setOccupiedBandwidth(int occupiedBandwidth) {
		this.occupiedBandwidth = occupiedBandwidth;
	}
	

	@Override
	protected QAM clone() throws CloneNotSupportedException {
		return (QAM) super.clone();
	}
	
	public enum SingalMode {
		DVB_C, DTMB, DVB_S;
	}
	
	public enum Definition {
		NONE, QAM16,QAM32,QAM64,QAM128,QAM256
	}
}