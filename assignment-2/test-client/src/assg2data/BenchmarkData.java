package assg2data;

public class BenchmarkData {
	long insert;
	long update; 
	long delete;

	public BenchmarkData(long insert, long update, long delete){
		this.insert = insert;
		this.update = update;
		this.delete = delete;
	}

	public long getInsert() {
		return insert;
	}

	public long getUpdate() {
		return update;
	}

	public long getDelete() {
		return delete;
	}
	
	
}
