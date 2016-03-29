package codes.monkey.springgraph

/**
 * Created by jzietsman on 3/18/16.
 */
class ImageFormat implements Format<byte[]> {

    Format<String> dotFormat = new DotFormat()

    String imageFormat = 'png'


    @Override
    byte[] apply(List<Node> targets) {
        def graphBytes = null
        File.createTempFile('graphviz', '.dot').with {
            write dotFormat.apply(targets)
            def output = "$parent/${name[0..-5]}-graph.$imageFormat"
            def command = "dot -T$imageFormat $absolutePath -o $output"
            command.execute().waitForOrKill(10000)
            new File(output).with {
                graphBytes = bytes
                delete()
            }
            delete()
        }
        graphBytes
    }
}
