
// Attempt at a simple handshake.  Girl pings Boy, gets confirmation.
// Then Boy pings girl, get confirmation.
class Monitor {
	String name;
	boolean flag;

	public Monitor(String name, boolean flag) {
		this.name = name;
		this.flag = flag;
	}

	public String getName() {
		return this.name;
	}

	// Girl thread invokes ping, asks Boy to confirm. But Boy invokes ping,
	// and asks Girl to confirm. Neither Boy nor Girl can give time to their
	// confirm call because they are stuck in ping. Hence the handshake
	// cannot be completed.

	public synchronized void ping(Monitor p) {
		this.notify();
		System.out.println(this.name + " (ping): pinging " + p.getName());
		if(p.flag){try {
			this.wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}}
		System.out.println(this.name + " (ping): asking " + p.getName() + " to confirm");
		p.confirm(this);
		System.out.println(this.name + " (ping): got confirmation");

	}

	public synchronized void confirm(Monitor p) {
		p.flag = false;
		System.out.println(this.name + " (confirm): confirm to " + p.getName());
		this.notify();
	}
}

class Runner extends Thread {
	Monitor m1, m2;

	public Runner(Monitor m1, Monitor m2) {
		this.m1 = m1;
		this.m2 = m2;
	}

	public void run() {
		m1.ping(m2);
	}
}

public class DeadLock {
	public static void main(String args[]) {
		int i = 1;
		System.out.println("Starting..." + (i++));
		Monitor a = new Monitor("Girl", true);
		Monitor b = new Monitor("Boy", false);
		(new Runner(a, b)).start();
		(new Runner(b, a)).start();
	}
}