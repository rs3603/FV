
	
public class CheckNPE {
	    public static void main(String[] args){
	        ResultList[] boll = new ResultList[5];
	        boll[0].name = "iiii";
	    }
	}
class ResultList {
	    public String name;
	    public Object value;
	    public ResultList() {}
	}
	