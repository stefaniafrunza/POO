import java.util.*;
import java.time.*;

public class Notification {
	enum NotificationType {
		EDIT, CANCEL
	};
	NotificationType nt; //nt = notification type
	LocalDateTime date; //date = data de trimitere a notificarii
	int id_cmpgn;
	LinkedList<Integer> voucherCodes;

	public Notification(NotificationType nt, LocalDateTime date, int id_cmpgn) {
		this.nt = nt;
		this.date = date;
		this.id_cmpgn = id_cmpgn;
		voucherCodes = new LinkedList<Integer>();
	}

	public String toString() {
		String result = this.id_cmpgn + ";" + this.voucherCodes + ";" + this.date + ";" + this.nt;
		return result;
	}
}