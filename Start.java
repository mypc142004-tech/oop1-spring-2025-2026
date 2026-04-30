interface IMemberOperation {
    double discountedFee();
}
 
abstract class Member {
    String memberID;
    double monthlyFee;
 
    Member() {}
 
    Member(String memberID, double monthlyFee) {
        this.memberID = memberID;
        this.monthlyFee = monthlyFee;
    }
 
    abstract void showInfo();
}
 
class PlatinumMember extends Member implements IMemberOperation {
    int freeSessions;
 
    PlatinumMember() {}
 
    PlatinumMember(String memberID, double monthlyFee, int freeSessions) {
        super(memberID, monthlyFee);
        this.freeSessions = freeSessions;
    }
 
    public double discountedFee() {
        if (monthlyFee > 8000) return monthlyFee * 0.92;
        return monthlyFee;
    }
 
    void showInfo() {
        System.out.println("Platinum: " + memberID + " " + discountedFee() + " " + freeSessions);
    }
}
 
class StandardMember extends Member implements IMemberOperation {
    boolean groupClassAccess;
 
    StandardMember() {}
 
    StandardMember(String memberID, double monthlyFee, boolean groupClassAccess) {
        super(memberID, monthlyFee);
        this.groupClassAccess = groupClassAccess;
    }
 
    public double discountedFee() {
        if (monthlyFee > 8000) return monthlyFee * 0.92;
        return monthlyFee;
    }
 
    void showInfo() {
        System.out.println("Standard: " + memberID + " " + discountedFee() + " " + groupClassAccess);
    }
}
 
class Gym {
    String name;
    Member[] mm;
    int count = 0;
 
    Gym() {}
 
    Gym(String name, int size) {
        this.name = name;
        mm = new Member[size];
    }
 
    void addMember(Member m) {
        mm[count++] = m;
    }
 
    void removeMember(int index) {
        mm[index] = null;
    }
 
    void showMembers() {
        for (Member m : mm) {
            if (m != null) m.showInfo();
        }
    }
 
    void totalRevenue() {
        double sum = 0;
        for (Member m : mm) {
            if (m != null) {
                IMemberOperation op = (IMemberOperation) m;
                sum += op.discountedFee();
            }
        }
        System.out.println("Total: " + sum);
    }
}
 
public class Start {
    public static void main(String[] args) {
        Member m1 = new PlatinumMember("P1", 9000, 5);
        Member m2 = new StandardMember("S1", 7000, true);
 
        Gym g = new Gym("FitZone", 5);
 
        g.addMember(m1);
        g.addMember(m2);
 
        g.showMembers();
        g.totalRevenue();
    }
}