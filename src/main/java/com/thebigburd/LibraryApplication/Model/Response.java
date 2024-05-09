package com.thebigburd.LibraryApplication.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(Include.NON_NULL)
public class Response {
	private final LocalDateTime timeStamp;
	private final int statusCode;
	private final HttpStatus status;
	private final String reason;
	private final String message;
	private final String devMessage;
	private final Map<?, ?> data;

	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public String getReason() {
		return reason;
	}

	public String getMessage() {
		return message;
	}

	public String getDevMessage() {
		return devMessage;
	}

	public Map<?, ?> getData() {
		return data;
	}

	@Override
	public String toString() {
		return "Response{" +
			"timeStamp=" + timeStamp +
			", statusCode=" + statusCode +
			", status=" + status +
			", reason='" + reason + '\'' +
			", message='" + message + '\'' +
			", devMessage='" + devMessage + '\'' +
			", data=" + data +
			'}';
	}

	private Response(Builder builder) {
		this.timeStamp = builder.timeStamp;
		this.statusCode = builder.statusCode;
		this.status = builder.status;
		this.reason = builder.reason;
		this.message = builder.message;
		this.devMessage = builder.devMessage;
		this.data = builder.data;
	}

	public static class Builder {
		private LocalDateTime timeStamp;
		private int statusCode;
		private HttpStatus status;
		private String reason;
		private String message;
		private String devMessage;
		private Map<?, ?> data;

		public Builder timeStamp(LocalDateTime timeStamp) {
			this.timeStamp = timeStamp;
			return this;
		}

		public Builder statusCode(int statusCode) {
			this.statusCode = statusCode;
			return this;
		}

		public Builder status(HttpStatus status) {
			this.status = status;
			return this;
		}

		public Builder reason(String reason) {
			this.reason = reason;
			return this;
		}

		public Builder message(String message) {
			this.message = message;
			return this;
		}

		public Builder devMessage(String devMessage) {
			this.devMessage = devMessage;
			return this;
		}

		public Builder data(Map<?, ?> data) {
			this.data = data;
			return this;
		}

		public Response build() {
			return new Response(this);
		}
	}
}
