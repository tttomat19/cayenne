/*****************************************************************
 *   Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 ****************************************************************/
package org.apache.cayenne.reflect.generic;

import org.apache.cayenne.di.Inject;
import org.apache.cayenne.map.EntityResolver;
import org.apache.cayenne.map.ObjEntity;
import org.apache.cayenne.reflect.AttributeProperty;
import org.apache.cayenne.reflect.ClassDescriptor;
import org.apache.cayenne.reflect.Property;
import org.apache.cayenne.reflect.PropertyVisitor;
import org.apache.cayenne.reflect.SingletonFaultFactory;
import org.apache.cayenne.reflect.ToManyProperty;
import org.apache.cayenne.reflect.ToOneProperty;
import org.apache.cayenne.unit.di.server.ServerCase;
import org.apache.cayenne.unit.di.server.UseServerRuntime;

@UseServerRuntime(ServerCase.INHERTITANCE_SINGLE_TABLE1_PROJECT)
public class DataObjectDescriptorFactory_InheritanceMapsTest extends ServerCase {

    @Inject
    private EntityResolver resolver;

    public void testVisitProperties_IterationOrder() {

        DataObjectDescriptorFactory factory = new DataObjectDescriptorFactory(
                resolver.getClassDescriptorMap(), new SingletonFaultFactory());

        for (ObjEntity e : resolver.getObjEntities()) {
            ClassDescriptor descriptor = factory.getDescriptor(e.getName());

            final Property[] lastProcessed = new Property[1];

            PropertyVisitor visitor = new PropertyVisitor() {

                public boolean visitToOne(ToOneProperty property) {
                    DataObjectDescriptorFactoryTest.assertPropertiesAreInOrder(
                            lastProcessed[0], property);
                    lastProcessed[0] = property;
                    return true;
                }

                public boolean visitToMany(ToManyProperty property) {
                    DataObjectDescriptorFactoryTest.assertPropertiesAreInOrder(
                            lastProcessed[0], property);
                    lastProcessed[0] = property;
                    return true;
                }

                public boolean visitAttribute(AttributeProperty property) {
                    DataObjectDescriptorFactoryTest.assertPropertiesAreInOrder(
                            lastProcessed[0], property);
                    lastProcessed[0] = property;
                    return true;
                }
            };

            descriptor.visitProperties(visitor);
        }

    }
}
