package com.woowahan.resources.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ykpark@woowahan.com
 */
@RestController
@RequestMapping("${server.error.path:${error.path:/error}}")
public class GlobalErrorMessageRestController extends AbstractErrorController {

    private final Logger LOGGER = LoggerFactory.getLogger(GlobalErrorMessageRestController.class);

    private final ErrorAttributes errorAttributes;
    private final ErrorProperties errorProperties;
    private final MessageSource messageSource;

    public GlobalErrorMessageRestController(ErrorAttributes errorAttributes, ErrorProperties errorProperties, MessageSource messageSource) {
        super(errorAttributes, Collections.emptyList());

        this.errorAttributes = errorAttributes;
        this.errorProperties = errorProperties;
        this.messageSource = messageSource;
    }

    @RequestMapping
    public ResponseEntity<ErrorMessage> error(HttpServletRequest request, Locale locale) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        ErrorMessage errorMessage = getErrorData(requestAttributes, request, locale);

        LOGGER.error(errorMessage.toString(), errorAttributes.getError(requestAttributes));

        return ResponseEntity.status(getStatus(request)).body(errorMessage);
    }

    private ErrorMessage getErrorData(RequestAttributes requestAttributes, HttpServletRequest request, Locale locale) {
        Map<String, Object> attributes = errorAttributes.getErrorAttributes(requestAttributes, isIncludeStackTrace(request));

        String path = (String) attributes.getOrDefault("path", null);
        HttpStatus status = getStatus(request);

        String message = messageSource.getMessage("error." + status, null, null, locale);
        if(Objects.nonNull(message)) {
            attributes.put("message", message);
        }

        List<ObjectError> errors = (List<ObjectError>) attributes.getOrDefault("errors", Collections.emptyList());

        return new ErrorMessage(status, message).path(path)
                                                .additionalInformation(
                                                    errors.stream().map(it -> {
                                                        Map<String, Object> error = new LinkedHashMap<>();
                                                        error.put("messageCode", Stream.of(it.getCodes()).collect(Collectors.joining(", ")));
                                                        error.put("message", messageSource.getMessage(it, locale));

                                                        return error;
                                                    }).collect(Collectors.toList())
                                                );
    }

    private boolean isIncludeStackTrace(HttpServletRequest request) {
        ErrorProperties.IncludeStacktrace include = errorProperties.getIncludeStacktrace();
        if (include == ErrorProperties.IncludeStacktrace.ALWAYS) {
            return true;
        }
        if (include == ErrorProperties.IncludeStacktrace.ON_TRACE_PARAM) {
            return getTraceParameter(request);
        }
        return false;
    }

    @Override
    public String getErrorPath() {
        return errorProperties.getPath();
    }

}
