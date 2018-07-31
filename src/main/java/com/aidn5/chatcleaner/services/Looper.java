package com.aidn5.chatcleaner.services;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Looper {

	public List<Runnable> runnableList;

	private int tickDelay = 40 * 60 * 15;
	private int tickCount = 0;

	public Looper() {
		runnableList = new ArrayList<Runnable>();
	}

	public void doTick() {
		tickCount++;
		if (tickCount < tickDelay) return;
		tickCount = 0;
		try {
			for (Iterator iterator = runnableList.iterator(); iterator.hasNext();) {
				Runnable runnable = (Runnable) iterator.next();
				new Thread(runnable).start();
			}
		} catch (Exception ignored) {}
	}

}
