package com.aspenshore.relish.core;

import org.junit.Test;

import static org.junit.Assert.*;

public class ComponentTest {
    @Test
    public void canGetValuesFromStringMethods() {
        ExampleComponent component = new ExampleComponent(null) {
            public String a() {
                return "I am A";
            }

            public String b() {
                return "I am B";
            }
        };
        assertEquals("I am A", component.get("a"));
        assertEquals("I am B", component.get("b"));
    }

    @Test
    public void canGetValuesFromComponentMethods() {
        ExampleComponent component = new ExampleComponent(null) {
            public ExampleComponent iAmAComponent() {
                return new ExampleComponent(null) {
                    @Override
                    public String getStringValue() {
                        return "Some value from a component";
                    }
                };
            }
        };
        assertEquals("Some value from a component", component.get("iAmAComponent"));
    }

    private static class ExampleComponent extends Component {

        /**
         * Instantiates a new Component.
         *
         * @param parent the component containing this
         */
        public ExampleComponent(Component parent) {
            super(parent);
        }

        @Override
        public void assertVisible() {

        }

        @Override
        public String getStringValue() {
            return null;
        }
    }
}

