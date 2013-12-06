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
package org.itstechupnorth.alfie.builders;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;

import org.itstechupnorth.alfie.Actor;
import org.itstechupnorth.alfie.Conductor;
import org.itstechupnorth.alfie.Engine;
import org.itstechupnorth.alfie.Message;




public class EngineSinkBuilder<Source, Result> {

	public Conductor build(Conductor conductor, BlockingQueue<Message<Source>> fromSource,
			Actor<Source, Result> actor, Actor<Result, ? extends Object> sinkActor) {
		final BlockingQueue<Message<Future<Result>>> toMediate = new LinkedBlockingDeque<Message<Future<Result>>>();
    	final Engine<Source, Result> engine = new Engine<Source, Result>(fromSource, toMediate, conductor.getExecutor(), actor);
    	conductor.manage(engine);
    	new HoldingSinkBuilder<Result>().build(sinkActor, conductor, toMediate);
		return conductor;
	}
}
