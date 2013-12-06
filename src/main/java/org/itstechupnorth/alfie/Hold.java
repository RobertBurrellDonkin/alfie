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

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Hold<Subject> extends MediaryCog<Future<Subject>, Subject> {

	private static final int COMPLETE_WAIT = 10;

	public static final int DEFAULT_MAX_TO_CHECK = 50;

	private final Queue<Future<Subject>> hold;

	public Hold(BlockingQueue<Message<Future<Subject>>> in,
			BlockingQueue<Message<Subject>> out) {
		this(in, out, DEFAULT_TIMEOUT, DEFAULT_MAX_TO_CHECK);
	}

	public Hold(BlockingQueue<Message<Future<Subject>>> in,
			BlockingQueue<Message<Subject>> out, int timeoutInMillis, final int maxToCheck) {
		super(in, out, timeoutInMillis);
		hold = new ArrayDeque<Future<Subject>>();
	}

	@Override
	protected void communicate(Future<Subject> future) {
		if (future == null) {
			System.out.println("Null future");
		} else if (future.isDone()) {
			forward(future);
		} else {
			hold.add(future);
		}
	}

	@Override
	protected void afterDoHook() {
		super.afterDoHook();
		checkFuture();
	}

	private void checkFuture() {
		//System.out.println("[CHECK] Start");
		while (!hold.isEmpty() && hold.peek().isDone()) {
			//System.out.println("[HOLD] " + hold.size());
			final Future<Subject> future = hold.poll();
			forward(future);
			//System.out.println("[HOLD] " + hold.size());
		}
		//System.out.println("[CHECK] End");
	}

	@Override
	protected void awaitingInputHook() {
		super.awaitingInputHook();
		forwardDone();
	}

	private void forwardDone() {
		//System.out.println("[IDLE] Start");
		//System.out.println("[HOLD] " + hold.size());
		for (final Iterator<Future<Subject>> it=hold.iterator(); it.hasNext();) {
			final Future<Subject> next = it.next();
			if (next.isDone()) {
				it.remove();
				forward(next);
			}
		}
		//System.out.println("[HOLD] " + hold.size());
		//System.out.println("[IDLE] End");
	}

	private void forward(Future<Subject> future) {
		try {
//			System.out.println("[FORWARD] Start");
			final Subject subject = future.get();
			out(subject);
	//		System.out.println("[FORWARD] Done");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof WithSeverity) {
				final WithSeverity withSeverity = (WithSeverity) cause;
				switch (withSeverity.severity()) {
				case WARNING:
					System.out.println("[FAIL] " + e.getMessage());
					break;
				case FATAL:
				default:
					printFullTraces(e);
					break;
				}
			} else {
				printFullTraces(e);
			}
		}
	}

	private void printFullTraces(ExecutionException e) {
		e.printStackTrace();
		e.getCause().printStackTrace();
	}

	@Override
	protected void completeWorkHook() {
		super.completeWorkHook();
		//System.out.println("Completing in flight...");
		while (!hold.isEmpty()) {
			try {
				hold.peek().get(COMPLETE_WAIT, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
			} catch (ExecutionException e) {
			} catch (TimeoutException e) {}
			//System.out.println("[COMPLETING] " + hold.size());
			forwardDone();
		}
		//System.out.println("Done.");
	}


}
