package app.vanadium.config.host;

import com.google.protobuf.ByteString;
import com.google.protobuf.Internal.EnumLite;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import app.vanadium.config.proto.VanadiumConfigProto.Component;
import app.vanadium.config.proto.VanadiumConfigProto.Component.AdditionalComponent;
import app.vanadium.config.proto.VanadiumConfigProto.Component.ComponentApkSpec;
import app.vanadium.config.proto.VanadiumConfigProto.Component.ComponentCondition;
import app.vanadium.config.proto.VanadiumConfigProto.Component.ComponentType;
import app.vanadium.config.proto.VanadiumConfigProto.Config;
import app.vanadium.config.proto.VanadiumConfigProto.Configs;
import app.vanadium.config.proto.VanadiumConfigProto.Flag;
import app.vanadium.config.proto.VanadiumConfigProto.Flag.FlagType;
import app.vanadium.config.proto.VanadiumConfigProto.Spec;
import app.vanadium.config.proto.VanadiumConfigProto.Spec.SpecType;

public class ConfigGenerator {

    private static final Configs getConfigs() {
        List<Config> configList = new ArrayList<>();
        configList.add(config(configParams -> configParams.setSpec(
                spec(specParams -> specParams.setSpecTypes(getSpecTypes(SpecType.BROWSER))
                ))
                .addAllComponents(components(
                        component(componentParams -> componentParams.setComponentType(ComponentType.SUBRESOURCE_FILTER_TOOLS)
                                .setComponentFileName("unindexed_ruleset")
                                .setVersionCode(1L)
                                .addAllAdditionalComponents(additionalComponents(
                                        additionalComponent(additionalComponentParams -> additionalComponentParams.setComponentFileName("unindexed_ruleset_germany")
                                                .addAllConditions(componentConditions(
                                                        componentCondition(componentConditionParams -> componentConditionParams.setLanguage("de"))
                                                ))
                                        )
                                ))
                        )
                ))
        ));
        return sortConfigs(configList);
    }

    public static void main(String[] args) throws IOException {
        String outFile = null;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if ("-o".equals(arg) || "--out_file".equals(arg)) {
                if (outFile != null) {
                    throw new IllegalArgumentException("Output file arguments can only be declared once");
                }
                outFile = args[i + 1];
                i++;
                continue;
            } else if (arg.startsWith("--out_file=")) {
                if (outFile != null) {
                    throw new IllegalArgumentException("Output file arguments can only be declared once");
                }
                outFile = arg.split("=")[1];
                i++;
                continue;
            }

            throw new IllegalArgumentException("Only arguments accepted are -o, --out_file");
        }

        if (outFile == null || outFile.isBlank() || outFile.isEmpty()) {
            throw new IllegalArgumentException("No output file supplied");
        }
        try (var outputStream = new FileOutputStream(outFile)) {
            getConfigs().writeTo(outputStream);
        }
    }

    private static Configs sortConfigs(List<Config> configList) {
        configList.sort((config, otherConfig) -> {
            Spec spec = config.getSpec();
            Spec otherSpec = otherConfig.getSpec();
            int specTypeCmp = Long.compare(spec.getSpecTypes(), otherSpec.getSpecTypes());
            if (specTypeCmp != 0) {
                return specTypeCmp;
            }

            int minVerCmp = Long.compare(spec.getMinVersion(), otherSpec.getMinVersion());
            if (minVerCmp != 0) {
                return minVerCmp;
            }

            int maxVerCmp = Long.compare(spec.getMaxVersion(), otherSpec.getMaxVersion());
            if (maxVerCmp != 0) {
                return maxVerCmp;
            }

            return 0;
        });
        return Configs.newBuilder().addAllConfigs(configList).build();
    }

    private static Config config(UnaryOperator<Config.Builder> configParams) {
        return configParams.apply(Config.newBuilder()).build();
    }

    private static Spec spec(UnaryOperator<Spec.Builder> specParams) {
        return specParams.apply(Spec.newBuilder()).build();
    }

    private static List<Flag> flags(Flag... flags) {
        return Arrays.asList(flags);
    }

    private static Flag flag(UnaryOperator<Flag.Builder> flagParams) {
        return flagParams.apply(Flag.newBuilder()).build();
    }

    private static List<Component> components(Component... components) {
        return Arrays.asList(components);
    }

    private static ComponentApkSpec componentApkSpec(UnaryOperator<ComponentApkSpec.Builder> componentApkSpecParams) {
        return componentApkSpecParams.apply(ComponentApkSpec.newBuilder()).build();
    }

    private static List<ByteString> certs(List<String> sha256Certs) {
        return sha256Certs.stream()
                .map(ByteString::fromHex)
                .collect(Collectors.toList());
    }

    private static Component component(UnaryOperator<Component.Builder> componentParams) {
        return componentParams.apply(Component.newBuilder()).build();
    }

    private static List<AdditionalComponent> additionalComponents(AdditionalComponent... additionalComponents) {
        return Arrays.asList(additionalComponents);
    }

    private static AdditionalComponent additionalComponent(
            UnaryOperator<AdditionalComponent.Builder> additionalComponentParams) {
        return additionalComponentParams.apply(AdditionalComponent.newBuilder()).build();
    }

    private static ComponentCondition componentCondition(
            UnaryOperator<ComponentCondition.Builder> componentConditionParams) {
        return componentConditionParams.apply(ComponentCondition.newBuilder()).build();
    }

    private static List<ComponentCondition> componentConditions(ComponentCondition... componentConditions) {
        return Arrays.asList(componentConditions);
    }

    private static long getSpecTypes(SpecType... specTypes) {
        return enumBitsLong(specTypes);
    }

    private static long enumBitsLong(EnumLite... enumBits) {
        long bits = 0L;
        for (var enumBit : enumBits) {
            bits |= 1L << enumBit.getNumber();
        }
        return bits;
    }
}
