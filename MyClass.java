
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;



class ToupleFloatInt {

	public float o1;
	public int o2;

	public ToupleFloatInt(float o1, int o2) {
		this.o1 = o1;
		this.o2 = o2;
	}

	public String toString() {
		return "(" + o1 + ", " + o2 + ")";
	}
}


class ToupleIntComparator implements Comparator<ToupleFloatInt> {
	@Override
	public int compare(ToupleFloatInt arg0, ToupleFloatInt arg1) {
		// these are ToupleFloatInts...sort on float value
		int thisV = ((ToupleFloatInt) arg0).o2;
		int thatV = ((ToupleFloatInt) arg1).o2;
		if(thisV < thatV) {
			return -1;
		} else if(thisV > thatV) {
			return 1;
		} else {
			return 0;
		}
	}

}

class ToupleFloatComparator implements Comparator<ToupleFloatInt> {
	@Override
	public int compare(ToupleFloatInt arg0, ToupleFloatInt arg1) {
		// these are ToupleFloatInts...sort on float value
		float thisV = ((ToupleFloatInt) arg0).o1;
		float thatV = ((ToupleFloatInt) arg1).o1;
		if(thisV < thatV) {
			return -1;
		} else if(thisV > thatV) {
			return 1;
		} else {
			return 0;
		}
	}
}

public class MyClass{

  public static void main(String[] args) {

      ArrayList<ToupleFloatInt> recs = new ArrayList<ToupleFloatInt>();
      for (int i = 0; i<10 ; i++ ) {
        recs.add(new ToupleFloatInt((1.0F*i), (12-i)));
      }
      // recs.add(new ToupleFloatInt(float(1.0), int(3)))

      Collections.sort(recs,  new ToupleIntComparator());
      System.out.println(recs);
      Collections.sort(recs, new ToupleFloatComparator());
      System.out.println(recs);

  }

}
