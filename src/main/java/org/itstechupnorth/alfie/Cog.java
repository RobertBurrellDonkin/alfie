/*
 *  Copyright 2010-2013 Robert Burrell Donkin
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.itstechupnorth.alfie;

import java.util.concurrent.CountDownLatch;

public abstract class Cog implements Runnable {

	private final CountDownLatch stopLatch = new CountDownLatch(1);

	protected abstract void doHook();

	private volatile boolean ready = true;
	private volatile boolean stop = false;

	public Cog() {
		super();
	}

	public final void run() {
		ready = false;
		try {
			while (!stop) {
				doHook();
			}
		} finally {
			stop = true;
			stopLatch.countDown();
			//System.out.println("[COG] Complete.");
		}
	}

	public final Cog finish() {
		stop = true;
		finishHook();
		return this;
	}

	protected void finishHook() {}

	public final Cog waitTillFinished() {
		try {
			stopLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}

	public void start() {
		if (ready) {
			new Thread(this).start();
		}
	}
}