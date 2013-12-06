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

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public abstract class QueueFedCog<Subject> extends Cog {

	public static final int DEFAULT_TIMEOUT = 100;

	protected abstract void communicate(final Subject subject);

	protected final BlockingQueue<Message<Subject>> in;
	private final int timeoutInMillis;

	public QueueFedCog(BlockingQueue<Message<Subject>> in) {
		this(in, DEFAULT_TIMEOUT);
	}

	public QueueFedCog(BlockingQueue<Message<Subject>> in, final int timeoutInMillis) {
		super();
		this.in = in;
		this.timeoutInMillis = timeoutInMillis;
	}

	@Override
	protected final void doHook() {
		try {
			if (in.isEmpty()) {
				awaitingInputHook();
			}
			final Message<Subject> message = in.poll(timeoutInMillis, TimeUnit.MILLISECONDS);
			if (message == null) {
				awaitingInputHook();
			} else {
				final Subject subject = message.getSubject();
				switch (message.getEvent()) {
				case COMMUNICATION:
					communicate(subject);
				break;
				case FATAL:
				case STOP:
					finish();
					break;
				}
			}
			afterDoHook();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void afterDoHook() {}

	protected void awaitingInputHook() {}

}