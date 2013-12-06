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
import org.itstechupnorth.alfie.Hold;
import org.itstechupnorth.alfie.Message;
import org.itstechupnorth.alfie.Sink;




public class HoldingSinkBuilder<Source> {

	public HoldingSinkBuilder() {
		super();
	}

	public Conductor build(Actor<Source, ? extends Object> sinkActor, Conductor conductor,
			BlockingQueue<Message<Future<Source>>> in) {
    	final BlockingQueue<Message<Source>> toSink = new LinkedBlockingDeque<Message<Source>>();
    	conductor.manage(new Hold<Source>(in, toSink));
		conductor.manage(new Sink<Source>(toSink, sinkActor));
		return conductor;
	}
}
