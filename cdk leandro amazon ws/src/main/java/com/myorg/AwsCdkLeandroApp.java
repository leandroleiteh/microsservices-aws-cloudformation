package com.myorg;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Environment;
import software.amazon.awscdk.StackProps;

public class AwsCdkLeandroApp {
    public static void main(final String[] args) {
        App app = new App();

        var vpcStack = new VpcStack(app, "Vpc", getStackProps());

        var clusterStack = new ClusterStack(app, "Cluster", getStackProps(), vpcStack.getVpc());
        clusterStack.addDependency(vpcStack);

        var rdsStack = new RdsStack(app, "Rds", getStackProps(), vpcStack.getVpc());
        rdsStack.addDependency(vpcStack);

        var service01Stack = new Service01Stack(app, "Service01", getStackProps(),clusterStack.getCluster());
        service01Stack.addDependency(clusterStack);
        service01Stack.addDependency(rdsStack);

        app.synth();
    }
    private static StackProps getStackProps(){
        return StackProps.builder()
                .env(Environment.builder()
                        .account("")
                        .region("us-east-1")
                        .build()).build();
    }
}

