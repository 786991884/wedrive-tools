package com.terminal.communication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ProtocolDispatcher {

    private static final Logger logger = LoggerFactory.getLogger(ProtocolDispatcher.class);

    private final Map<Integer, TaCommand> handlers;

    @Autowired
    public ProtocolDispatcher(List<TaCommand> handlerList) {
        Map<Integer, TaCommand> map = new HashMap<>();
        for (TaCommand handler : handlerList) {
            Class<? extends TaCommand> clazz = handler.getClass();
            if (!clazz.isAnnotationPresent(LocationCommand.class)) {
                continue;
            }
            LocationCommand locationCommand = clazz.getAnnotation(LocationCommand.class);
            Integer key = locationCommand.id();
            logger.info("{} loading handler : {}", key, handler.getClass());
            map.put(key, handler);
        }
        logger.info("load proto count:{}", map.size());
        handlers = Collections.unmodifiableMap(map);
    }

    public TaCommand getHandler(int cmdId) {
        return handlers.get(cmdId);
    }

}
