package hello.advanced.trace.hellotrace;

import org.springframework.stereotype.Component;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HelloTraceV2 {


	private static final String START_PREFIX = "-->";
	private static final String COMPLETE_PREFIX = "<--";
	private static final String EX_PREFIX = "<x-";

	public TraceStatus begin(String message) {
		TraceId traceId = new TraceId();
		Long startTimeMs = System.currentTimeMillis();

		log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

		return new TraceStatus(traceId, startTimeMs, message);
	}

	//V2에서 추가
	public TraceStatus beginSync(TraceId beforeTraceId, String message) {
		TraceId nextId = beforeTraceId.createNextId();
		Long startTimeMs = System.currentTimeMillis();

		log.info("[{}] {}{}", nextId.getId(), addSpace(START_PREFIX, nextId.getLevel()), message);

		return new TraceStatus(nextId, startTimeMs, message);
	}

	public void end(TraceStatus traceStatus) {
		complete(traceStatus, null);
	}

	public void exception(TraceStatus traceStatus, Exception e) {
		complete(traceStatus, e);
	}

	private void complete(TraceStatus traceStatus, Exception e) {
		Long stopTimeMs = System.currentTimeMillis();
		long resultTImeMs = stopTimeMs - traceStatus.getStartTimeMs();
		TraceId traceId = traceStatus.getTraceId();

		if (e == null) {
			log.info("[{}] {}{} time={}ms", traceId.getId(), addSpace(COMPLETE_PREFIX, traceId.getLevel()), traceStatus.getMessage(), resultTImeMs);
		} else {
			log.info("[{}] {}{} time={}ms ex={}", traceId.getId(), addSpace(EX_PREFIX, traceId.getLevel()), traceStatus.getMessage(), resultTImeMs, e.toString());
		}
	}

	private String addSpace(String prefix, int level) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < level; i++) {
			sb.append((i == level - 1) ? "|" + prefix : "|   ");
		}

		return sb.toString();
	}

}
