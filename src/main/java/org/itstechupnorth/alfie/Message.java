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

public class Message<Subject> {

	public enum Event{COMMUNICATION, STOP, FATAL}

	private final Subject subject;
	private final Event event;
	private final Throwable cause;

	public Message(Throwable cause) {
		this(null, Event.FATAL, cause);
	}

	public Message() {
		this(null, Event.STOP, null);
	}

	public Message(Subject subject) {
		this(subject, Event.COMMUNICATION, null);
	}

	private Message(Subject subject, Event event, final Throwable cause) {
		super();
		this.subject = subject;
		this.event = event;
		this.cause = cause;
	}

	public Throwable getCause() {
		return cause;
	}

	public Subject getSubject() {
		return subject;
	}

	public Event getEvent() {
		return event;
	}

	@Override
	public String toString() {
		return "Message [event=" + event + ", subject=" + subject + "]";
	}
}
