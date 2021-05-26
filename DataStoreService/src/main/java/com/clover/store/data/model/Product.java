package com.clover.store.data.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

//https://ducmanhphan.github.io/2020-04-06-how-to-apply-builder-pattern-with-inhertitance/
@JsonDeserialize(builder = Product.ProductBuilder.class)
public class Product {
    private  int a;
    private  String b;
    protected Product(final ProductBuilder<?> builder){
        this.a = builder.a;
        this.b = builder.b;
    }

    public  int getA() {
        return a;
    }

    public  String getB() {
        return b;
    }

    @JsonPOJOBuilder(buildMethodName = "build", withPrefix = "")
    public static class ProductBuilder<T extends ProductBuilder<T>> {
        private  int a;
        private  String b;

        public ProductBuilder(){}

        public T a(int a) {
            this.a = a;
            return (T)this;
        }

        public T b(String b) {
            this.b = b;
            return (T)this;
        }

        public Product build(){
            return new Product(this);
        }
    }
}
