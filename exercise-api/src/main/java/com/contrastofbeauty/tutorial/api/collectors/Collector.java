package com.contrastofbeauty.tutorial.api.collectors;

import com.contrastofbeauty.tutorial.api.domain.Callback;

/**
 * A collector provides a transparent data structure able to collect objects of a given type with respect a user id.
 * Once the list of objects for a given user id reaches the buffer size the flush method is invoked to empty the list
 * and pass the objects via a callback function.
 * <p/>
 * The callback function must be provided in order to make the flush method work.
 */
public interface Collector {

    /**
     * The given buffer size.
     */
    public static final int PROCESSING_LIST_BUFFER_SIZE = 100;

    /**
     * Accept just objects of type T and nothing else. If the object is accepted is then added to a data structure with
     * respect to the id of the user.
     *
     * @param object the object to be accepted or discarded
     * @param userId the id of the user
     * @return true if the object is accepted and then addde to the data structure of the collector, false otherwise.
     */
    boolean accept(final Object object, final long userId);

    /**
     * Invoke the callback function to pass all the objects of the list for the given user id and then empty the list of
     * the collector.
     *
     * @param userId the id of the user to select the list of objects
     */
    void flush(final long userId);

    /**
     * Execute some operations after the flush method is invoked.
     *
     * @param userId the id of the user
     */
    void postFlush(final long userId);

    /**
     * Set the callback function.
     *
     * @param callback the callback function to be set
     */
    void setCallbackFunction(final Callback callback);

    /**
     * A default buffer size or threshold is given by the interface but it can be overridden by this setter.
     *
     * @param bufferSize the new size of the buffer
     */
    void setNewBufferSize(final int bufferSize);

    /**
     * Return the size of the list for a given user id.
     *
     * @param userId the id of the user
     * @return the size of the list
     */
    int getListSizeByUserId(final long userId);
}
