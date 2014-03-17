package thread;

import main.MakeSound;
import main.ZombieHouse;

public class SoundThread extends Thread {

	private String filepath;
	private boolean loop = false;
	private boolean play = true;
	private MakeSound ms;

	public SoundThread(String file, boolean loop) {
		this.filepath = file;
		this.loop = loop;
		this.ms = new MakeSound();
	}

	public void run() {
		if (loop) {
			while (play) {
				ms.playSound(filepath);
			}
		}else{
			ms.playSound(filepath);
		}
	}

	public void stopLoop(){
		this.play = false;
	}
}
