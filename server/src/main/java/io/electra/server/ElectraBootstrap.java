/*
 * MIT License
 *
 * Copyright (c) 2017 Felix Klauke, JackWhite20
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package io.electra.server;

import io.electra.core.ElectraCore;
import io.electra.core.ElectraCoreFactory;
import io.electra.server.binary.ElectraBinaryServer;
import io.electra.server.rest.RestServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Philip 'JackWhite20' <silencephil@gmail.com>
 */
public class ElectraBootstrap {

    private static Logger logger = LoggerFactory.getLogger(ElectraBootstrap.class);

    private static RestServer restServer;

    private static ElectraBinaryServer electraBinaryServer;

    public static void main(String[] args) {
        logger.info("Starting electra");

        ElectraCore electraCore = ElectraCoreFactory.createElectraCore();

        restServer = new RestServer(electraCore);
        restServer.start();

        electraBinaryServer = new ElectraBinaryServer(electraCore);
        electraBinaryServer.start();

        //logger.info("Electra started");
    }
}
