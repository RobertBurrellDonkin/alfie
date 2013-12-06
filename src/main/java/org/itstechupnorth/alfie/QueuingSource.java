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

public abstract class QueuingSource<Result> implements Source {

	private final BlockingQueue<Message<Result>> out;

	public QueuingSource(BlockingQueue<Message<Result>> out) {
		this.out = out;
	}

	protected final void fatal(Throwable cause) {
		try {
			out.put(new Message<Result>(cause));
			close();
		} catch (InterruptedException e) {
			e.printStackTrace();
			fatal(cause);
		}
	}

	protected final void output(final Result result) {
		try {
			out.put(new Message<Result>(result));
		} catch (InterruptedException e) {
			e.printStackTrace();
			output(result);
		}
	}

	public final void close() {
		hookForClose();
		try {
			out.put(new Message<Result>());
		} catch (InterruptedException e) {
			e.printStackTrace();
			close();
		}
	}

	protected void hookForClose() {}
}