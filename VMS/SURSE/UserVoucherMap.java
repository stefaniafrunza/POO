import java.util.*;

public class UserVoucherMap extends ArrayMap<Integer, LinkedList<Voucher>> {
	public boolean addVoucher(Voucher v) {
		for(int i : this.keySet()) {
			if(this.get(i).contains(v) == true)
				return false;
		}

		int ok = 0;
		for(int i : this.keySet()) {
			if(i == v.id_cmpgn)
				ok = 1;
		}

		if(ok == 0)
			return false;

		this.get(v.id_cmpgn).add(v);
		return true;
	}
}