package kieker.tpmon.writer;

import java.util.Vector;
import kieker.common.record.IMonitoringRecord;
import kieker.tpmon.writer.util.async.AbstractWorkerThread;

/**
 * kieker.tpmon.IKiekerMonitoringLogWriter
 * 
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 *
 * @author Andre van Hoorn
 */
public interface IKiekerMonitoringLogWriter {

    public boolean isWriteRecordTypeIds();

    public void setWriteRecordTypeIds(boolean writeRecordTypeIds);

    public void registerMonitoringRecordType(int id, String className);

    public boolean writeMonitoringRecord(IMonitoringRecord monitoringRecord);

    public boolean init(String initString);

    /**
     * Returns a vector of workers, or null if none.
     */
    public Vector<AbstractWorkerThread> getWorkers();

    public void setDebug(boolean debug);

    public boolean isDebug();

    public String getInfoString();
}
