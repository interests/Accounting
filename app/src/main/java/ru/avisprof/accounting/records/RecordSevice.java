package ru.avisprof.accounting.records;

import java.util.Date;

/**Вспомогательный класс, чтобы группировать записи - по датам
 * Created by Leonid on 20.04.2016.
 */
public class RecordSevice {
	private int mRecordID;
	private long mDate;
	private boolean mHeader;
	private double mSum;
	
	public int getRecordID() {
		return mRecordID;
	}
	public void setRecordID(int recordID) {
		this.mRecordID = recordID;
	}
	public long getDate() {
		return mDate;
	}
	public void setDate(long date) {
		this.mDate = date;
	}
	public boolean isHeader() {
		return mHeader;
	}
	public void setHeader(boolean header) {
		this.mHeader = header;
	}


	public double getSum() {
		return mSum;
	}

	public void setSum(double sum) {
		this.mSum = sum;
	}
}
