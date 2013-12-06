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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;


public class Engine<Subject, Result> extends MediaryCog<Subject, Future<Result>> {

	private final ExecutorService executor;
	private final Actor<Subject, Result> actor;
	public Engine(BlockingQueue<Message<Subject>> in,
			ExecutorService executor, Actor<Subject, Result> actor) {
		this(in, null, executor, actor);
	}

	public Engine(BlockingQueue<Message<Subject>> in,
			final BlockingQueue<Message<Future<Result>>> out,
			ExecutorService executor, Actor<Subject, Result> actor) {
		super(in, out, DEFAULT_TIMEOUT);
		this.executor = executor;
		this.actor = actor;
	}

	@Override
	protected void communicate(final Subject subject) {
		if (subject != null) {
			final Future<Result> future = executor.submit(new Action<Subject, Result>(subject, actor));
			postSubmit(future);
		}
	}

	private void postSubmit(Future<Result> future) {
		out(future);
		postSubmitHook(future);
	}

	protected void postSubmitHook(Future<Result> future) {}
}
