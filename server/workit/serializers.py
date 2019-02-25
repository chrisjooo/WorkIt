from django.contrib.auth.models import User, Group
from rest_framework import serializers
from workit.models import Biodata, Exercise


class UserSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = User
        fields = ('url', 'username', 'email', 'groups')


class GroupSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Group
        fields = ('url', 'name')


class ExerciseSerializer(serializers.ModelSerializer):
    class Meta:
        model = Exercise
        fields = ('id', 'date', 'pushup', 'situp', 'jogging')

class BiodataSerializer(serializers.ModelSerializer):
    class Meta:
        model = Biodata
        fields = ('id', 'weight', 'name', 'dateofbirth', 'height')