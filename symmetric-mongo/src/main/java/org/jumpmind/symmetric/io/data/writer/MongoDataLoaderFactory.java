/**
 * Licensed to JumpMind Inc under one or more contributor
 * license agreements.  See the NOTICE file distributed
 * with this work for additional information regarding
 * copyright ownership.  JumpMind Inc licenses this file
 * to you under the GNU General Public License, version 3.0 (GPLv3)
 * (the "License"); you may not use this file except in compliance
 * with the License.
 *
 * You should have received a copy of the GNU General Public License,
 * version 3.0 (GPLv3) along with this library; if not, see
 * <http://www.gnu.org/licenses/>.
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jumpmind.symmetric.io.data.writer;

import java.util.List;

import org.jumpmind.db.platform.IDatabasePlatform;
import org.jumpmind.extension.IBuiltInExtensionPoint;
import org.jumpmind.symmetric.ISymmetricEngine;
import org.jumpmind.symmetric.db.ISymmetricDialect;
import org.jumpmind.symmetric.ext.ISymmetricEngineAware;
import org.jumpmind.symmetric.io.data.IDataWriter;
import org.jumpmind.symmetric.load.DefaultDataLoaderFactory;

public class MongoDataLoaderFactory extends DefaultDataLoaderFactory implements
        ISymmetricEngineAware, IBuiltInExtensionPoint {

    protected ISymmetricEngine engine;

    protected String typeName = "mongodb";

    protected IDBObjectMapper objectMapper;

    public MongoDataLoaderFactory() {
        super();
    }

    @Override
    public void setSymmetricEngine(ISymmetricEngine engine) {
        this.engine = engine;
        this.parameterService = engine.getParameterService();
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    @Override
    public IDataWriter getDataWriter(String sourceNodeId, ISymmetricDialect symmetricDialect,
            TransformWriter transformWriter, List<IDatabaseWriterFilter> filters,
            List<IDatabaseWriterErrorHandler> errorHandlers,
            List<? extends Conflict> conflictSettings, List<ResolvedData> resolvedData) {
        if (objectMapper instanceof SimpleDBObjectMapper) {
            ((SimpleDBObjectMapper)objectMapper).setDefaultDatabaseName(parameterService.getString("mongodb.default.databasename", "default"));
        }
        return new MongoDatabaseWriter(objectMapper, new SimpleMongoClientManager(parameterService, typeName),
                new DefaultTransformWriterConflictResolver(transformWriter),
                buildDatabaseWriterSettings(filters, errorHandlers, conflictSettings, resolvedData));
    }

    @Override
    public boolean isPlatformSupported(IDatabasePlatform platform) {
        return true;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public void setObjectMapper(IDBObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

}
