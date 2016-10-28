package com.jlccaires.sourcemanager.service;

import java.io.File;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.io.input.Tailer;
import org.apache.commons.io.input.TailerListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ConsoleService {

	@Value("${server.logfile.location:''}")
	private String logFile;
	private Tailer tailer;
	private LogListener logListener;

	@PostConstruct
	public void console() {
		final File log = new File(logFile);
		if (logFile.isEmpty() || !log.exists()) {
			return;
		}
		tailer = new Tailer(log, new TailerListenerAdapter() {
			public void handle(String line) {
				if (logListener != null) {
					logListener.onNewLine(line);
				}
			}

			public void handle(Exception ex) {
				if (logListener != null) {
					logListener.onNewLine(ex.getMessage());
				}
			}
		}, 1000, true);
		Thread thread = new Thread(tailer);
		thread.setDaemon(true);
		thread.start();
	}

	@PreDestroy
	public void destroy() throws Exception {
		if (tailer != null) {
			tailer.stop();
		}
	}

	public void setLogListener(LogListener logListener) {
		this.logListener = logListener;
	}

	public interface LogListener {
		void onNewLine(String line);
	}

}
