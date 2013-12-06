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

import java.util.concurrent.Callable;


public class Action<Subject, Result> implements Callable<Result> {

	private final Subject subject;
	private final Actor<Subject, Result> actor;

	public Action(Subject file, Actor<Subject, Result> processor) {
		super();
		this.subject = file;
		this.actor = processor;
	}

	public Result call() throws Exception {
		try {
			return actor.act(subject);
		} catch (Exception e) {
			System.out.println("[FAIL] error when processing " + subject + " (" + e.getMessage() + ")");
			throw e;
		}
	}
}
