package com.myorg;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.ec2.InstanceType;
import software.amazon.awscdk.services.ec2.*;
import software.amazon.awscdk.services.rds.*;
import software.constructs.Construct;

import java.util.Collections;


public class RdsStack extends Stack {
    public RdsStack(final Construct scope, final String id, Vpc vpc) {
        this(scope, id, null, vpc);
    }

    public RdsStack(final Construct scope, final String id, final StackProps props, Vpc vpc) {
        super(scope, id, props);

        var databasePassword = CfnParameter.Builder.create(this, "databasePassword")
                .type("String")
                .description("The RDS instance password")
                .build();

        var iSecurityGroup = SecurityGroup.fromSecurityGroupId(this, id, vpc.getVpcDefaultSecurityGroup());
        iSecurityGroup.addEgressRule(Peer.anyIpv4(), Port.tcp(3306));


        var databaseInstance = DatabaseInstance.Builder.create(this, "Rds01")
                .instanceIdentifier("aws-project-o1-db")
                .engine(DatabaseInstanceEngine.mysql(MySqlInstanceEngineProps.builder()
                        .version(MysqlEngineVersion.VER_8_0)
                        .build()))
                .vpc(vpc)
                .credentials(Credentials.fromUsername("admin", CredentialsFromUsernameOptions.builder()
                        .password(SecretValue.unsafePlainText(databasePassword.getValueAsString()))
                        .build()))
                .instanceType(InstanceType.of(InstanceClass.T3, InstanceSize.MICRO))
                .allowMajorVersionUpgrade(true)
                .multiAz(false)
                .allocatedStorage(10)
                .securityGroups(Collections.singletonList(iSecurityGroup))
                .vpcSubnets(SubnetSelection.builder()
                        .subnets(vpc.getPrivateSubnets())
                        .build())
                .build();

        CfnOutput.Builder.create(this, "rds-endpoint")
                .exportName("rds-endpoint")
                .value(databaseInstance.getDbInstanceEndpointAddress())
                .build();

        CfnOutput.Builder.create(this, "rds-password")
                .exportName("rds-password")
                .value(databasePassword.getValueAsString())
                .build();

    }
}
