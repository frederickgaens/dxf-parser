package be.codesolutions.resolve;

import be.codesolutions.dxf.DxfBlocks;
import be.codesolutions.dxf.DxfDocument;
import be.codesolutions.dxf.DxfHeader;
import be.codesolutions.dxf.beans.DxfAttribute;
import be.codesolutions.dxf.beans.DxfBlockEntity;
import be.codesolutions.dxf.beans.DxfPair;
import be.codesolutions.dxf.entities.DxfPoint;
import be.codesolutions.enums.DxfSection;
import be.codesolutions.enums.DxfTags;
import be.codesolutions.io.DxfReader;
import be.codesolutions.tools.Traverse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Resolve dxf file.
 */

public class DxfRvlFile {

    private List<DxfAttribute> dxf;
    private DxfReader reader;

    /**
     * Init objects.
     */
    public DxfRvlFile() {
        reader = new DxfReader();
    }

    /**
     * Begin start resolve dxf file.
     *
     * @param file dxf file path.
     * @return return DxfDocument object.
     */
    public DxfDocument resolve(String file) {
        try {
            dxf = reader.dxf2Obj(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return startResolve();
    }

    /**
     * Begin resolve dxf object list.
     *
     * @return return DxfDocument object.
     */
    private DxfDocument startResolve() {
        DxfDocument doc = new DxfDocument();
        doc.setHeader(this.resolveHeader(this.splitOfSection(DxfSection.HEADER)));
        doc.setBlocks(this.resolveBlocks(this.splitOfSection(DxfSection.BLOCKS)));
//        for (DxfAttribute attribute : this.splitOfSection(DxfSection.BLOCKS)) {
//            System.out.println("\"" + attribute.getGroupCode() + "\":\"" + attribute.getGroupValue() + "\"");
//        }
        return doc;
    }

    /**
     * Resolve dxf blocks.
     *
     * @param dxf dxf object list.
     * @return return DxfBlocks object.
     */
    private DxfBlocks resolveBlocks(List<DxfAttribute> dxf) {
        DxfBlocks blocks = new DxfBlocks();
        List<DxfBlockEntity> entities = new ArrayList<>();
        List<DxfAttribute> blcEntity = new ArrayList<>();
        DxfRvlGraph graph = new DxfRvlGraph();
        for (DxfAttribute attribute : dxf) {
            if (attribute.getGroupCode() == 0 && attribute.getGroupValue().equals(DxfTags.SECTION.name())) continue;
            if (attribute.getGroupCode() == 0 && attribute.getGroupValue().equals(DxfTags.ENDSEC.name())) continue;
            if (attribute.getGroupCode() == 2 && attribute.getGroupValue().equals(DxfSection.BLOCKS.name())) continue;
            if (attribute.getGroupCode() == 0 && attribute.getGroupValue().equals(DxfTags.BLOCK.name())) {
                if (blcEntity.size() != 0) {
                    entities.add(graph.rvlBlockEntity(blcEntity));
                    blcEntity.clear();
                }
            }
            blcEntity.add(attribute);
        }
        if (blcEntity.size() != 0) entities.add(graph.rvlBlockEntity(blcEntity));
        blocks.setBlockEntities(entities);
        return blocks;
    }

    /**
     * Resolve dxf header.
     *
     * @param dxf dxf object list.
     * @return return DxfHeader object.
     */
    private DxfHeader resolveHeader(List<DxfAttribute> dxf) {
        DxfHeader header = new DxfHeader();
        List<DxfPair> pairs = new ArrayList<>();
        Traverse traverse = new Traverse(dxf);
        DxfAttribute attr1 = null, attr2;
        traverse.begin();
        while (traverse.hasNext()) {
            if (attr1 == null) attr1 = traverse.next().obj();
            if (!traverse.hasNext()) break;
            attr2 = traverse.next().obj();
            if (attr1.getGroupValue().equals(DxfTags.SECTION.name())) {
                attr1 = null;
            } else if (attr2.getGroupCode() != 10) {
                DxfPair pair = new DxfPair();
                pair.setName(attr1.getGroupValue());
                pair.setValue(attr2.getGroupValue());
                pairs.add(pair);
                attr1 = null;
            } else {
                DxfPair pair = new DxfPair();
                pair.setName(attr1.getGroupValue());
                DxfPoint point = new DxfPoint();
                point.setX(Double.parseDouble(attr2.getGroupValue()));
                point.setY(Double.parseDouble(traverse.next().obj().getGroupValue()));
                attr1 = traverse.next().obj();
                if (attr1.getGroupCode() == 30) {
                    point.setY(Double.parseDouble(attr1.getGroupValue()));
                    attr1 = null;
                }
                pair.setValue(point);
                pairs.add(pair);
            }
        }
        traverse.end();
        header.setVars(pairs);
        return header;
    }

    /**
     * Split object list with tag.
     *
     * @param tag split tag.
     * @return return dxf object list split  with tag.
     */
    private List<DxfAttribute> splitOfSection(DxfSection tag) {
        List<DxfAttribute> part = new ArrayList<>();
        int subStart = 0, subEnd = 0;
        for (int i = 0; i < dxf.size(); i++) {
            DxfAttribute attribute = dxf.get(i);
            if (attribute.getGroupCode() == 0 && attribute.getGroupValue().equals(DxfTags.SECTION.name())) {
                DxfAttribute name = dxf.get(i + 1);
                if (name.getGroupCode() == 2 && name.getGroupValue().equals(tag.name())) {
                    part.add(attribute);
                    subStart = i;
                }
            } else if (attribute.getGroupCode() == 0 && attribute.getGroupValue().equals(DxfTags.ENDSEC.name())) {
                if (part.size() > 0) {
                    part.add(attribute);
                    subEnd = i + 1;
                    break;
                }
            } else {
                if (part.size() > 0) {
                    part.add(attribute);
                }
            }
        }
        if (part.size() > 0) dxf.subList(subStart, subEnd).clear();
        return part;
    }
}
