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

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Cogs {

	private final Collection<Cog> cogs;
	private final Source source;

	public Cogs(final Source source) {
		super();
		cogs = new ConcurrentLinkedQueue<Cog>();
		this.source = source;
	}

	public Cogs manage(Cog cog) {
		cogs.add(cog);
		return this;
	}

	public Cogs start() {
		for (final Cog cog:cogs) {
			cog.start();
		}
		return this;
	}

	public Cogs perform() {
		source.open();
		return this;
	}

	public Cogs finish() {
		source.close();
		for (final Cog cog:cogs) {
			//System.out.println("[CONDUCTOR] Waiting for Cog to finish...");
			cog.waitTillFinished();
		}
		return this;
	}

}