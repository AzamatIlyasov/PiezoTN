package tn.fxgraph.graph;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

public class MouseGestures {

    final DragContext dragContext = new DragContext();

    Graph graph;

    public MouseGestures( Graph graph) {
        this.graph = graph;
    }

    public void makeDraggable( final Node node) {

        node.setOnMousePressed(onMousePressedEventHandler);
        node.setOnMouseDragged(onMouseDraggedEventHandler);
        node.setOnMouseReleased(onMouseReleasedEventHandler);
        //node.setOnMouseEntered(OnMouseEnteredEventHandler);
        node.setOnMouseClicked(OnMouseClickedEventHandler);

    }

    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Node node = (Node) event.getSource();

            double scale = graph.getScale();

            dragContext.x = node.getBoundsInParent().getMinX() * scale - event.getScreenX();
            dragContext.y = node.getBoundsInParent().getMinY()  * scale - event.getScreenY();

        }
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Node node = (Node) event.getSource();

            double offsetX = event.getScreenX() + dragContext.x;
            double offsetY = event.getScreenY() + dragContext.y;

            // adjust the offset in case we are zoomed
            double scale = graph.getScale();

            offsetX /= scale;
            offsetY /= scale;

            node.relocate(offsetX, offsetY);

        }
    };

    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

        }
    };

/* //движение мышки
    EventHandler<MouseEvent> OnMouseEnteredEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Cell node = (Cell) event.getSource();
            System.out.println(node.cellId);

        }
    };
*/

// кликнул по элементу - получил доп инфу
    EventHandler<MouseEvent> OnMouseClickedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Cell node = (Cell) event.getSource();
            graph.setHydraData(node.getHydraData());
            System.out.println("");
            System.out.print(node.cellId);
            System.out.print(" ");
            System.out.print(node.hydraDataClassStruct.ZdanieEtaj);
            System.out.print(" ");
            System.out.print(node.hydraDataClassStruct.Hrasp_endP);
            System.out.print(" ");

            System.out.println("");
        }
    };

    class DragContext {

        double x;
        double y;

    }
}
