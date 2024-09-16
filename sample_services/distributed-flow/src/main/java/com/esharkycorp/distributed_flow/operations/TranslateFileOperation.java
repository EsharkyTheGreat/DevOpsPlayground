package com.esharkycorp.distributed_flow.operations;


import com.esharkycorp.distributed_flow.models.TranslationInfo;
import com.esharkycorp.distributed_flow.responses.TranslationResponse;
import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractConsumerResponse;
import com.hilabs.cdi.common.queuebuilder.abstracts.AbstractDataHandler;
import com.hilabs.cdi.common.queuebuilder.abstracts.FatalException;
import com.hilabs.cdi.common.queuebuilder.abstracts.RetryException;
import com.hilabs.cdi.common.queuebuilder.abstracts.WarningException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TranslateFileOperation extends AbstractDataHandler<TranslationInfo> {
    @Override
    public TranslationResponse process(TranslationInfo data) throws RetryException, FatalException, WarningException {
        log.info("Translating File : {}",data);
        return new TranslationResponse();
    }
}
