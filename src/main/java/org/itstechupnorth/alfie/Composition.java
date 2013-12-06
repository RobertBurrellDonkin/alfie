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

public class Composition<Subject, Mediate, Result> implements Actor<Subject, Result> {

	private final Actor<Subject, Mediate> first;
	private final Actor<Mediate, Result> second;

	public Composition(Actor<Subject, Mediate> first,
			Actor<Mediate, Result> second) {
		super();
		this.first = first;
		this.second = second;
	}

	public Result act(Subject subject) throws Exception {
		return second.act(first.act(subject));
	}

	public void finish() {
		first.finish();
		second.finish();
	}

}
