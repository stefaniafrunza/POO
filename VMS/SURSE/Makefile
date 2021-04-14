JFLAGS = -g

JC = javac
.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java

CLASSES = \
				Campaign.java \
				LoyalityVoucher.java \
				VMS.java \
				Test.java \
				Notification.java \
				User.java \
				Voucher.java \
				GiftVoucher.java \
				ArrayMap.java \
				CampaignVoucherMap.java \
				UserVoucherMap.java

build: classes

classes: $(CLASSES:.java=.class)

run: build
	java Test

clean:
	$(RM) *.class