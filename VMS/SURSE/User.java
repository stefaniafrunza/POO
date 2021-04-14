import java.util.*;

public class User {
	int id;
	String name;
	String email;
	String password;
	enum UserType {
		ADMIN, GUEST
	};
	UserType type;
	UserVoucherMap vouchers;
	LinkedList<Notification> notifications;

	public User(int id, String name, String email, String password, UserType type) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.type = type;
		this.vouchers = new UserVoucherMap();
		this.notifications = new LinkedList<Notification>();
	}

	public String toString() {
		return this.id + ";" + this.name + ";" + this.email + ";" + this.type;
	}

	public void update(Notification notification) { //Observer pattern
		Notification not;
		not = new Notification(notification.nt, notification.date, notification.id_cmpgn);

		for(Voucher voucher : this.vouchers.get(notification.id_cmpgn)) {
			not.voucherCodes.add(voucher.id);
		}

		this.notifications.add(not);
	}
}