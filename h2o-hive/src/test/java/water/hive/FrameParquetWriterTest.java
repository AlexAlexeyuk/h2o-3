package water.hive;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import water.Scope;
import water.fvec.Frame;
import water.runner.CloudSize;
import water.runner.H2ORunner;

import java.io.File;
import java.io.IOException;

import static water.TestUtil.assertFrameEquals;
import static water.TestUtil.parse_test_file;

@RunWith(H2ORunner.class)
@CloudSize(1)
public class FrameParquetWriterTest {

    @Rule
    public TemporaryFolder tmp = new TemporaryFolder();
    
    @Test
    public void testSaveFrame() throws IOException {
        Scope.enter();
        try {
            Frame fr = Scope.track(parse_test_file("./smalldata/airlines/AirlinesTrain.csv"));
            File parquetFile = tmp.newFile("prostate.parquet");
            parquetFile.delete();
            new FrameParquetWriter().write(fr, parquetFile.getAbsolutePath());
            Frame fromParquet = Scope.track(parse_test_file(parquetFile.getAbsolutePath()));
            assertFrameEquals(fr, fromParquet, 1e-10);
        } finally {
            Scope.exit();
        }
    }

}
