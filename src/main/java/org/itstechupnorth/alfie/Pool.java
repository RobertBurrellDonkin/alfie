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

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Pool<Subject, Result> implements Actor<Subject, Result> {

	private final ActorFactory<Subject, Result> factory;
	private final int size;
	private final BlockingQueue<Actor<Subject, Result>> queue;
	private final CountDownLatch activeActors;
	private boolean open = false;

	public Pool(ActorFactory<Subject, Result> factory, int size) {
		super();
		this.factory = factory;
		this.size = size;
		queue = new ArrayBlockingQueue<Actor<Subject, Result>>(size);
		activeActors = new CountDownLatch(size);
	}

	public synchronized Pool<Subject, Result> open() {
		if (!open) {
			for (int i=0;i<size;i++) {
				queue.add(factory.build());
			}
		}
		return this;
	}

	public Result act(Subject subject) throws Exception {
		final Actor<Subject, Result> actor = queue.take();
		try {
			return actor.act(subject);
		} finally {
			queue.put(actor);
		}
	}

	public void finish() {
		while (activeActors.getCount() > 0) {
			Actor<Subject, Result> actor;
			try {
				actor = queue.take();
				activeActors.countDown();
				actor.finish();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
