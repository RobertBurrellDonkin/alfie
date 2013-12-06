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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Conductor {

	private static final int DEFAULT_TIMEOUT_IN_MINUTES = 10;

	private final Cogs cogs;
	private final ExecutorService executor;

	public Conductor(final int threads, final Source source) {
		super();
		cogs = new Cogs(source);
		executor = Executors.newFixedThreadPool(threads, Executors.defaultThreadFactory());
	}

	public ExecutorService getExecutor() {
		return executor;
	}

	public Conductor manage(Cog cog) {
		cogs.manage(cog);
		return this;
	}

	public Conductor start() {
		cogs.start();
		return this;
	}

	public Conductor perform() {
		cogs.perform();
		return this;
	}

	public Conductor finish() {
		cogs.finish();
		//System.out.println("[CONDUCTOR] Shutting down thread pool...");
		executor.shutdown();
		try {
			executor.awaitTermination(DEFAULT_TIMEOUT_IN_MINUTES, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return this;
	}
}
