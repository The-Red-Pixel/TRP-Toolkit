package work.erio.toolkit.module;

import com.google.gson.Gson;
import work.erio.toolkit.util.ClassUtils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModuleManager {

    private int index;
    private static ModuleManager instance;
    private List<IModule> moduleList;

    public ModuleManager() {
//        this.moduleList = Arrays.asList(
//                new ModuleTest(),
//                new ModuleComparator(),
//                new ModuleBlockData()
//        );
        this.moduleList = new ArrayList<>();
        this.loadModules();
        deserialize();
        this.index = 0;
    }

    private void loadModules() {
        try {
            List<Class> classes = ClassUtils.findAllClasses("work.erio.toolkit.module");
            for (Class c : classes) {
                if (c != IModule.class && Arrays.stream(c.getInterfaces()).anyMatch(i -> i == IModule.class)) {
                    this.moduleList.add((IModule) c.newInstance());
                }
            }
        } catch (URISyntaxException | IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void serialize() {
        ModuleBean[] moduleBeans = getEnabledModuleList()
                .parallelStream()
                .map(m -> new ModuleBean(m.getClass().getSimpleName()))
                .toArray(ModuleBean[]::new);
        Gson gson = new Gson();

        try {
            Writer writer = new FileWriter("toolkit_modules.json");
            gson.toJson(moduleBeans, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deserialize() {
        Gson gson = new Gson();
        File file = new File("toolkit_modules.json");
        if (!file.exists()) {
            return;
        }
        try {
            Reader reader = new FileReader("toolkit_modules.json");
            List<String> moduleNames = Arrays.stream(gson.fromJson(reader, ModuleBean[].class))
                    .map(ModuleBean::getName)
                    .collect(Collectors.toList());
            this.getFullModuleList().forEach(m -> {
                if (!moduleNames.contains(m.getClass().getSimpleName())) {
                    ((AbstractModule) m).setEnabled(false);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void addModule(IModule module) {
        if (moduleList.contains(module)) {
            moduleList.add(module);
        }
    }

    public List<IModule> getFullModuleList() {
        return moduleList;
    }

    public List<IModule> getEnabledModuleList() {
        Stream<IModule> enabledModuleStream = moduleList.stream().filter(m -> ((AbstractModule) m).isEnabled());
        List<IModule> enabledModules = enabledModuleStream.collect(Collectors.toList());
        return enabledModules;
    }

    public static ModuleManager getInstance() {
        if (instance == null) {
            instance = new ModuleManager();
        }
        return instance;
    }

    public void nextModule() {
        Stream<IModule> enabledModuleStream = moduleList.stream().filter(m -> ((AbstractModule) m).isEnabled());
        List<IModule> enabledModules = enabledModuleStream.collect(Collectors.toList());
        int index = this.index >= enabledModules.size() - 1 ? 0 : this.index + 1;
        this.index = index;
    }

    public void prevModule() {
        Stream<IModule> enabledModuleStream = moduleList.stream().filter(m -> ((AbstractModule) m).isEnabled());
        List<IModule> enabledModules = enabledModuleStream.collect(Collectors.toList());
        int index = this.index <= 0 ? enabledModules.size() - 1 : this.index - 1;
        this.index = index;
    }

    public IModule getCurrentModule() {
        Stream<IModule> enabledModuleStream = moduleList.stream().filter(m -> ((AbstractModule) m).isEnabled());
        List<IModule> enabledModules = enabledModuleStream.collect(Collectors.toList());
        if (enabledModules.isEmpty()) {
            return null;
        }
        return enabledModules.get(this.index);
    }



    public IModule getModuleByClass() {
        return null;
    }
}
