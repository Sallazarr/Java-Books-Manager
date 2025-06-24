package Models;

import java.util.List;

public record VolumeInfo(String title, List<String> authors, String description, Integer pageCount){}
