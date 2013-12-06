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

public class Sink<Subject> extends QueueFedCog<Subject> {

	private final Actor<Subject, ? extends Object> actor;

	public Sink(BlockingQueue<Message<Subject>> in,
			Actor<Subject, ? extends Object> actor) {
		super(in);
		this.actor = actor;
	}

	@Override
	protected void communicate(Subject subject) {
		try {
			//System.out.println("[SINK] start");
			actor.act(subject);
			//System.out.println("[SINK] end");
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void finishHook() {
		super.finishHook();
		//System.out.println("[SINK] Finishing");
		actor.finish();
		//System.out.println("[SINK] Finished");
	}

}
