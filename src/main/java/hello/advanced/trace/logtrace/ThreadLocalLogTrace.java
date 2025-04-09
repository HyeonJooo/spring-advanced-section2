package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceId;
import hello.advanced.trace.TraceStatus;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadLocalLogTrace implements LogTrace {

	private static final String START_PREFIX = "-->";
	private static final String COMPLETE_PREFIX = "<--";
	private static final String EX_PREFIX = "<x-";

	private ThreadLocal<TraceId> traceIdHolder = new ThreadLocal<>();

	@Override
	public TraceStatus begin(String message) {
		syncTraceId();
		TraceId traceId = traceIdHolder.get();
		Long startTimeMs = System.currentTimeMillis();

		log.info("[{}] {}{}", traceId.getId(), addSpace(START_PREFIX, traceId.getLevel()), message);

		return new TraceStatus(traceId, startTimeMs, message);
	}

	private void syncTraceId() {
		TraceId traceId = traceIdHolder.get();

		if (traceId == null) {
			traceIdHolder.set(new TraceId());
		} else {
			traceIdHolder.set(traceId.createNextId());
		}
	}

	@Override
	public void end(TraceStatus status) {
		complete(status, null);
	}

	@Override
	public void exception(TraceStatus status, Exception e) {
		complete(status, e);
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

		releaseTraceId();
	}

	private void releaseTraceId() {
		TraceId traceId = traceIdHolder.get();

		if (traceId.isFirstId()) {
			traceIdHolder.remove(); //destroy
		} else {
			traceIdHolder.set(traceId.createPreviousId());
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
