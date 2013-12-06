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

public abstract class MediaryCog<Subject, Result> extends QueueFedCog<Subject> {

	private final BlockingQueue<Message<Result>> out;

	public MediaryCog(BlockingQueue<Message<Subject>> in,
			final BlockingQueue<Message<Result>> out,
			int timeoutInMillis) {
		super(in, timeoutInMillis);
		this.out = out;
	}

	@Override
	protected final void finishHook() {
		completeWorkHook();
		if (out != null) {
			try {
				out.put(new Message<Result>());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void completeWorkHook() {}

	protected void out(Result result) {
		if (out != null) {
			try {
				//System.out.println("[OUT] Put...");
				out.put(new Message<Result>(result));
				//System.out.println("[OUT] Done.");
			} catch (InterruptedException e) {
				e.printStackTrace();
				out(result);
			}
		}
	}

}